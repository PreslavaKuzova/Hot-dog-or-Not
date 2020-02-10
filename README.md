# Hot-dog-or-Not
The name of the project is inspired by the famous sitcom *Silicon valley*. **HotdogOrNot** is a console food analyzer application, allowing the user to choose between multiple ways for searching food related data. A number of requests can be sent simultaneously providing the following endpoint options available:
* Search food data by name
* Search nutriets by food's unique identifier 
* Search food data by image
* Seach food by barcode image or barcode number

## Utils
This package contains helper classes inluding **constants** and **io**. Here we keep the different types of devices with the help of which we manage the input/output data flow.
### Constants
This is where all the constants that the application is going to use are kept. They are devided into separate classes depending on their usage - *CacheConstants, ClientConstants & FoodAnalyzerConstants*
### IO
Here we keep the information about the devices the application is going to support. Currently, the data can be read by and written in either the console, when *ConsoleDevice* is implemented, or any two given .txt files with the help of *FileDevice*. The common thing between them is that they both implement the *IODevice* interface. 
* **IODevice** - a simple interface that declares the *String read()* and *void write(String data)* methods that should be overridden. 
* **FileDevice** - it allows the user to read the data from and write the output into a file. It has a private *BlockingQueue* member which stores all the queries of the given input file. 
* **ConsoleDevice** - the data that is going to be processed is coming from the console and this is also where the output will be presented once the query is done with it's execution.

## Presenters
This package contains the data presenter that is the link between the input/output configuration - the **IODevice** and the method implementation **IOController**. This is where the user queries are parsed correctly. This is also where the data, returned by the controller, is being displayed either in the console (when using *ConsoleDevice*), or in a file in a given directory (when using *FileDevice*). It creates an inifite loop expecting commands up until *exit application* is being processed. Currently only one presenter is available, *IOPresenter* but can easily be extended in the future.

## Controllers
This package is the link between the method implementation, meaning it manages the different clients, and the presenter (*IOPresenter*). As in the begining only *one* intance is created, it is safe that despite the fact it will manage many threads, only one instance of a client is going to be created. In it's constructor it takes an instance of an *IODevice* and links itself to the presenter available. Every method creates a new separate thread that runs the given method, meaning many requests of the same type can be processed simultaneously. 
* ***public void onFoodRequestMade(String food)*** - sends a request to the networkClient and returns a *List* containing *Food* instances if any match the required search parameter
* ***public void onNutrientsRequestMade(String fdcId)*** - sends a request to the networkClient and returns *NutritionalValues* if any match the required unique food identifier
* ***public void onBarcodeRequestMade(String barcode)*** - checks if there is a *Food* instance already loaded in the *BarcodeCache* and returns it so. Requests can be done by providing one of the following parameters: 
  * a directory to a photo file containing a barcode image
  * an unique barcode code
* ***public void onPhotoRequestMade(String directory)*** - sends a request to the *ImageRecognitionClient* and returs a list of food names contained in the provided photo 

## Data
### Model
This package contains the required data transfer objects. It contains the following models, that save the needed information, parsed from the json, and mothods, that help us extract the data in human-readable way. Those are the base of the application's structure:  
* ***Food***
* ***NutritionalValues*** - this class uses the following objects, that are all sublassses of the *NutrientBase* model:
  * Calories
  * Carbohydrates
  * Fat
  * Protein
### Cache
This package keeps the information, returned by previous queries. All the caches implemented are extending the generic abstract class *LFUCache* that contains the base implementation of a least-frequently used cache. All of the methods are synchronized, meaning that despite many threads are going to operate with the same instance of the particular cache, no information is going to be corrupted. All of the implementations are using the *Singleton design pattern* which ensures that all of the clients will operate with the same instance of the given cache
  * ***FoodCache*** - saves infomation about all the queries regarding the get-food command so whenever the same parameter is given the result is being returned immediately to the user instead of making a request to the server
  * ***NutrientsCache*** - saves information about the ingredients and the nutritional value of any particular food so whenever the unique identifier is requested again the result can be returned immediately
  * ***BarcodeCache*** - saves the information about the barcode of every single Food object (whenever such information is avaibale is the returned repsonse) while the get-food command is being executed
### Client
This package is where the logic of the application is being held. It contains the implementation of the client side, manages the requests that are being sent to the APIs and manages the reponses respectively. Every class manages different types of requests.
  *  ***NetworkClient*** - this class is respsonsible for the management of the requests sent to the *U.S. Department of Agriculture* database *FoodData Central*. It manages two different types of responses - for particular food and for nutritional values. It is being extended by the ***CachedNetworkClient*** class that also manages the usage of the cache - saves the information in it or returns result if one is already available
  * ***ImageRecognitionClient*** - send photo in *base64* format to the **Google Vision API**, which on it's behalf returns the most commonly related labels related to the provided photo. With the help of this then the client can easily manage the *get-food* requests on their own.
  * ***BarcodeClient*** - this class is responsible for returning the information, related to a particular barcode or barcode photo, of a food if one is already stored in the cache. It can accept both parameters and can parse it respectively. If a photo is being provided, then the **ZXing** library is being used to return the barcode, saved on the image. 
