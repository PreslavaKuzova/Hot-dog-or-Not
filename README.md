# Hot-dog-or-Not
A food analyzer with a twist, allowing the user multiple ways for searching food related data. The options available are the following:
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
  * unique barcode code
* ***public void onPhotoRequestMade(String directory)*** - sends a request to the *ImageRecognitionClient* and returs a list of food names contained in the provided photo 
