## 🐕  AI Lost Pet Search App 
- _The PDF file is a study note for AI Lost Pet Search App._ <br/> <br/> <br/> 

### 1. &nbsp; Background of Development <br/> 
- _Recently, the number of dog owners in Korea is increasing. According to statistics from the "Animal Freedom Solidarity," more than 10 million people own dogs. Unfortunately, the number of abandoned animals is also increasing according to this trend.When dog owners lose their dogs, they are very worried about what to do. At this time, the first thing to do is to go to a place where the dog is likely to be and find it. I devised a project to solve these problems more easily._ <br/><br/><br/>

### 2. &nbsp; Project Introduction <br/> 
- _This application is an SNS-type application that helps dog owners easily find their lost dogs._  <br/><br/> 
  - _If a passerby finds an abandoned dog wandering alone on the street, the passerby can post information such as the place and time of discovery along with a picture of the dog._ <br/>
    - _It's okay not to have knowledge of dog breeds when writing a post._ <br/>
    - _This is because the application automatically informs the dog's breed information using the YOLO model._ <br/><br/>
  - _At this time, users who register posts will be given 500 points._ <br/>
    - _For users who also take protective measures, 50,000 points will be given._ <br/><br/>
  - _On the contrary, owners who lose their dogs have to pay 100 points to access the information in the post._ <br/>
    - _In the case of user posts that are even taking protective measures, they have to pay 5000 points._ <br/><br/>
- _In addition, the application provides free information on abandoned dog shelters in Gyeonggi-do._ <br/><br/><br/>
 

### 3. &nbsp; Main Function <br/> <br/> 
- _**Sign Up & Sign In**_ <br/> <br/>
<img src="https://github.com/qortmdgh4141/AI_Lost_Pet_Search_App/blob/main/image/main_registration_login.png?raw=true"  width="640" height="240"> <br/> <br/> <br/>

- _**The Process of Registering a Post**_ <br/> <br/>
<img src="https://github.com/qortmdgh4141/AI_Lost_Pet_Search_App/blob/main/image/registering_a_post.png?raw=true"  width="1280" height="300"> <br/> <br/> <br/>

- _**Bulletin Board of Abandoned Dog Shelter Located in Gyeonggi-Do**_ <br/> <br/>
<img src="https://github.com/qortmdgh4141/AI_Lost_Pet_Search_App/blob/main/image/the_bulletin_board_of_the_abandoned_dog_shelter.png?raw=true"  width="960" height="380"> <br/> <br/> <br/>


### 4. &nbsp; YOLO Model Training Strategies Using Transfer-Learning & Fine-Tuning <br/> <br/>

- _**Transfer-Learning & Fine-Tuning Definition**_
  - _Transfer learning consists of taking features learned on one problem, and leveraging them on a new, similar problem._
  - _Transfer learning is usually done for tasks where your dataset has too little data to train a full-scale model from scratch._
  - _The most common incarnation of transfer learning in the context of deep learning is the following workflow._
    1. _Take layers from a previously trained model._
    2. _Freeze them, so as to avoid destroying any of the information they contain during future training rounds._
    3. _Add some new, trainable layers on top of the frozen layers. They will learn to turn the old features into predictions on a new dataset._
    4. _Train the new layers on your dataset._
  - _A last, optional step, is fine-tuning, which consists of unfreezing the entire model you obtained above (or part of it), and re-training it on the new data with a very low learning rate._ <br/> <br/> 
  
  
 - _**My Fine-Tuning Strategy**_ <br/> <br/>
 
|Strategy| &nbsp; &nbsp; &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Method |&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Feature|
|:-----------------------:|:-----------------------|:-----------------------|
|_**Strategy 1**_|_Train the entire model. In this situation, it is possible to use the architecture of the pre-trained model and train it according to the dataset._|_It is recommended for large datasets._|
|_**Strategy 2**_|_Train some layers and leave the others frozen. In a CNN architecture, lower layers refer to general features (problem independent), while higher layers refer to specific features (problem dependent). In this case, we have to adjust the weights of the network._|_This option is useful when we have a small dataset and a large number of parameters, we need to leave more layers frozen to avoid overfitting. On the other hand, if the dataset is large and the number of parameters is small, it is possible to improve the model by training more layers to the new task._|
|_**Strategy 3**_|_Freeze the convolutional base. In this situation, we have an extreme case of the train/freeze trade-off. The rationale behind it is to keep the original form of the convolutional base to use as input for the classifier. By this way, the pre-trained model plays the role of a feature extractor._|_It can be interesting for small datasets or if the problem solved by the pre-trained model is similar to the one we are working on._| 

<br/> 

 - _**Results Based on 3 Strategies**_ <br/> <br/>
  <img src="https://github.com/qortmdgh4141/AI_Lost_Pet_Search_App/blob/main/image/transfer_learning_fine_tuning.png?raw=true"  width="1280" height="340"> <br/> <br/> 
    - _**Strategy 1** &nbsp; : &nbsp; Yellow, &nbsp;&nbsp;&nbsp;&nbsp; **Strategy 2 &nbsp; :** &nbsp; Pink, &nbsp;&nbsp;&nbsp;&nbsp; **Strategy 3 &nbsp; :** &nbsp; Purple_  <br/> 
    - _Strategy 1 shows the best results._
    - _I think the reasons for this result are as follows._
    - _The dataset I used is a large dataset and has little resemblance to the dataset of pre-trained models_ <br/> <br/> <br/>

--------------------------
### 💻 S/W Development Environment
<p>
  <img src="https://img.shields.io/badge/Windows 10-0078D6?style=flat-square&logo=Windows&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android Studio-3DDC84?style=flat-square&logo=Android&logoColor=white"/> 
  <img src="https://img.shields.io/badge/Firebase-blue?style=flat-square&logo=Firebase&logoColor=FFCA28"/>
</p>  
<p>
  <img src="https://img.shields.io/badge/PyCharm-66FF00?style=flat-square&logo=PyCharm&logoColor=black"/>
  <img src="https://img.shields.io/badge/NVIDIA-black?style=flat-square&logo=NVIDIA&logoColor=76B900"/>
  <img src="https://img.shields.io/badge/Anaconda-e9e9e9?style=flat-square&logo=Anaconda&logoColor=44A833"/>
</p>
<p>
  <img src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=Python&logoColor=white"/>
  <img src="https://img.shields.io/badge/PyTorch-FF9900?style=flat-square&logo=PyTorch&logoColor=EE4C2C"/>
  <img src="https://img.shields.io/badge/NumPy-013243?style=flat-square&logo=Numpy&logoColor=blue"/>
  <img src="https://img.shields.io/badge/Java-FF0000?style=flat-square&logo=Java&logoColor=white"/> 
</p>   

### 🚀 Deep Learning Model
<p>
  <img src="https://img.shields.io/badge/YOLO-black?&logo=YOLO&logoColor=00FFFF"/>
</p>

### 💾 Datasets used in the project
- _**AI Hub Dataset &nbsp; : &nbsp;** Animal videos to distinguish pets._
  -  https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=115&topMenu=100&aihubDataSe=realm&dataSetSn=59
- _**Gyeonggi Data Dream Dataset &nbsp; : &nbsp;** Status of Abandoned Animal Protection._
  -  https://data.gg.go.kr/portal/data/service/selectServicePage.do?page=1&sortColumn=&sortDirection=&infId=UOKOBXSYKT10BAGIDAXZ28522406&infSeq=1&searchWord=%EC%9C%A0%EA%B8%B0+%EB%8F%99%EB%AC%BC+%EB%B3%B4%ED%98%B8+%ED%98%84%ED%99%A9
