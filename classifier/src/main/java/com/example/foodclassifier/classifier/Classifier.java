package com.example.foodclassifier.classifier;

import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import org.apache.tvm.Device;
import org.apache.tvm.Function;
import org.apache.tvm.Module;
import org.apache.tvm.NDArray;
import org.apache.tvm.TVMType;
import org.apache.tvm.TVMValue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

/**
 * Main Class for Food Classifier.
 */


public class Classifier {

    private static final String MODEL_GRAPH_FILE_PATH = "resources/rawfile/graph.json";

    private static final String MODEL_CPU_LIB_FILE_PATH = "resources/rawfile/deploy_lib.so";
    private static final String MODEL_CPU_LIB_FILE_NAME = "deploy_lib.so";

    private static final String MODEL_PARAMS_FILE_PATH = "resources/rawfile/params.bin";

    private static final String LABEL_PATH = "resources/rawfile/food_labels.txt";
    private static final String LABEL_NAME = "food_labels.txt";

    // TVM constants
    private int outputIndex = 0;
    private int imgChannel = 3;
    private String inputName = "input";
    private int modelInputSize = 192;
    private String imagePath;
    private String imageName;
    String label = null;
    ohos.global.resource.ResourceManager resManager;
    File cachedir;

    /**
     * Classifier Constructor.
     *
     * @param path - path for input image
     * @param name - name of input image
     * @param resm - ResourceManager GetResourceManager()
     * @param cachedir    - CacheDir()
     */

    public Classifier(String path, String name, ResourceManager resm, File cachedir) {
        this.imagePath = path;
        this.imageName = name;
        this.resManager = resm;
        this.cachedir = cachedir;
        this.label = runFoodClassification();
    }

    public String getOutput() {
        return label;
    }

    /**
     * Main Function to run food classifier.
     */
    public String runFoodClassification() {

        // load json graph
        String modelGraph = null;
        RawFileEntry rawFileEntryModel = resManager.getRawFileEntry(MODEL_GRAPH_FILE_PATH);
        try {
            modelGraph = new String(getBytesFromRawFile(rawFileEntryModel));
        } catch (IOException e) {
            return null; //failure
        }

        // create java tvm device
        Device tvmDev = Device.cpu();

        RawFileEntry rawFileEntryModelLib = resManager.getRawFileEntry(MODEL_CPU_LIB_FILE_PATH);
        File file = null;
        Module modelLib = null;
        try {
            file = getFileFromRawFile(MODEL_CPU_LIB_FILE_NAME, rawFileEntryModelLib, cachedir);
            modelLib = Module.load(file.getAbsolutePath());
        } catch (NullPointerException | IOException e) {
            return null; //failure
        }

        Function runtimeCreFun = Function.getFunction("tvm.graph_executor.create");
        TVMValue runtimeCreFunRes = runtimeCreFun.pushArg(modelGraph)
                .pushArg(modelLib)
                .pushArg(tvmDev.deviceType)
                .pushArg(tvmDev.deviceId)
                .invoke();
        Module graphExecutorModule = runtimeCreFunRes.asModule();

        // load parameters
        byte[] modelParams = null;
        RawFileEntry rawFileEntryModelParams = resManager.getRawFileEntry(MODEL_PARAMS_FILE_PATH);
        try {
            modelParams = getBytesFromRawFile(rawFileEntryModelParams);
        } catch (IOException e) {
            return null; //failure
        }

        // get the function from the module(load parameters)
        Function loadParamFunc = graphExecutorModule.getFunction("load_params");
        loadParamFunc.pushArg(modelParams).invoke();

        RawFileEntry rawFileEntryImage = resManager.getRawFileEntry(imagePath);
        File fileImage = null;
        try {
            fileImage = getFileFromRawFile(imageName, rawFileEntryImage, cachedir);
        } catch (IOException e) {
            return null; //failure
        }

        ImageSource imageSource = ImageSource.create(fileImage, null);
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(192, 192);
        PixelMap pixelMap = imageSource.createPixelmap(decodingOpts);

        IntBuffer imageBuffer = IntBuffer.allocate(pixelMap.getImageInfo().size.height
                * pixelMap.getImageInfo().size.width);
        pixelMap.readPixels(imageBuffer);

        // image RGB values
        byte[] imgRgbValues = new byte[modelInputSize * modelInputSize * imgChannel];

        // image pixel int values
        int[] pixelValues = imageBuffer.array();

        for (int j = 0; j < pixelValues.length; ++j) {
            imgRgbValues[j * 3 + 0] = (byte) (pixelValues[j] & 0xFF);
            imgRgbValues[j * 3 + 1] = (byte) ((pixelValues[j] >> 8) & 0xFF);
            imgRgbValues[j * 3 + 2] = (byte) ((pixelValues[j] >> 16) & 0xFF);
        }

        // get the function from the module(set input data)
        NDArray inputNdArray = NDArray.empty(new long[]{1, imgChannel,
            modelInputSize, modelInputSize}, new TVMType("uint8"));

        inputNdArray.copyFromRaw(imgRgbValues);
        Function setInputFunc = graphExecutorModule.getFunction("set_input");
        setInputFunc.pushArg(inputName).pushArg(inputNdArray).invoke();
        // release tvm local variables
        inputNdArray.release();
        setInputFunc.release();

        // get the function from the module(run it)
        Function runFunc = graphExecutorModule.getFunction("run");
        runFunc.invoke();
        // release tvm local variables
        runFunc.release();

        // get the function from the module(get output data)
        NDArray outputNdArray = NDArray.empty(new long[]{1, 2024}, new TVMType("uint8"));
        Function getOutputFunc = graphExecutorModule.getFunction("get_output");
        getOutputFunc.pushArg(outputIndex).pushArg(outputNdArray).invoke();
        byte[] output = outputNdArray.internal();
        // release tvm local variables
        outputNdArray.release();
        getOutputFunc.release();

        // display the result from extracted output data
        String outputlabel = null;
        if (null != output) {
            int maxPosition = -1;
            float maxValue = 0;
            int[] ret = new int[output.length];
            for (int j = 0; j < output.length; ++j) {
                ret[j] = output[j] & 0xff;
                if (ret[j] > maxValue) {
                    maxValue = ret[j];
                    maxPosition = j;
                }
            }

            RawFileEntry rawFileEntrylabelfile = resManager.getRawFileEntry(LABEL_PATH);
            FileInputStream fin = null;
            File ifile = null;
            try {
                ifile = getFileFromRawFile(LABEL_NAME, rawFileEntrylabelfile, cachedir);
                fin = new FileInputStream(ifile);
                BufferedReader bufferedreader;
                int i = 0;
                String[] labelarray = new String[(int) ifile.length()];
                String line;
                try (InputStreamReader inputstreamreader = new InputStreamReader(fin)) {
                    bufferedreader = new BufferedReader(inputstreamreader);
                    while ((line = bufferedreader.readLine()) != null) {
                        labelarray[i++] = line;
                    }
                    outputlabel = labelarray[maxPosition];
                }
            } catch (IOException e) {
                return null; //failure
            }
        }
        return outputlabel;
    }

    private static File getFileFromRawFile(String filename, RawFileEntry rawFileEntry, File cacheDir)
            throws IOException {
        byte[] buf = null;
        File file;
        file = new File(cacheDir, filename);
        try (FileOutputStream output = new FileOutputStream(file)) {
            Resource resource = rawFileEntry.openRawFile();
            buf = new byte[(int) rawFileEntry.openRawFileDescriptor().getFileSize()];
            int bytesRead = resource.read(buf);
            if (bytesRead != buf.length) {
                throw new IOException("Asset Read failed!!!");
            }
            output.write(buf, 0, bytesRead);
            return file;
        }
    }

    private static byte[] getBytesFromRawFile(RawFileEntry rawFileEntry)
            throws IOException {
        byte[] buf = null;
        try {
            Resource resource = rawFileEntry.openRawFile();
            buf = new byte[(int) rawFileEntry.openRawFileDescriptor().getFileSize()];
            int bytesRead = resource.read(buf);
            if (bytesRead != buf.length) {
                throw new IOException("Asset Read failed!!!");
            }
            return buf;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
