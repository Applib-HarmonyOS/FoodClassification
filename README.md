# Food Classifier
A deep learning based food classification model which can identify upto 2000+ food categories across the globe.

### App Screenshots
<p>
	<img src="/screenshots/foodclassifier_1.jpeg" width = 350 ></img>
        <img src="/screenshots/foodclassifier_2.jpeg" width = 350 ></img>
        <img src="/screenshots/foodclassifier_3.jpeg" width = 350 ></img>

</p>

# Source
This library has been inspired by [TensorflowHub\\Food Classifier](https://tfhub.dev/google/lite-model/aiy/vision/classifier/food_V1/1).

## Integration
 1. For using FoodClassifier module in sample app, include the source code and add the below dependencies in entry/build.gradle to generate hap/support.har.

```
	implementation project(path: ':classifier')
```

 2. For using FoodClassifier module in separate application using har file, add the har file in the entry/libs folder and add the dependencies in entry/build.gradle file.

```
	implementation fileTree(dir: 'libs', include: ['*.har'])
```
 3. For using FoodClassifier module from a remote repository in separate application, add the below dependencies in entry/build.gradle file.

```
	implementation 'dev.applibgroup:classifier:1.0.0'
```

## Usage
 1. Initialise the constructor of Classifier with the image path, image name, getResourceManager() and getCacheDir() arguments.
 
 2. Use get_output() to get the string label of the classified food image.
Example:

```slice
    	Classifier myclassifier = new Classifier(MODEL_INPUT_IMAGE_PATH, MODEL_INPUT_IMAGE_NAME, 
    			getResourceManager(), getCacheDir());
    	outputlabel = myclassifier.get_output();
```
Check the example app for more information.

## License

	Copyright (c) TensorFlow Hub

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

