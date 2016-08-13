#### UPDATE 2-29-2016: Added ``@Deprecated``, more constructors, and exception handlers

# Generic AsyncTask
This is a custom Android generic AsyncTask class that performs a POST call from Android App to Web Server and get the response's result back.

#### Now you can watch the tutorial of using this class on YouTube at https://www.youtube.com/watch?v=e4eFBQmWs8Q ####

## What For?
In Android App, usually when you want to read data in text, XML, or JSON format from a web service, you need to have a private class that extends AsyncTask class. So that will create a big chunk of codes, i.e. private class, in your existing codes.

Hence, this ``PostResponseAsyncTask`` class will help you to cut out the private class. You can read and write (POST) by using the same class with a different constructor call. Because it is async-ed, to get a callback result, you let your class implement ``AsyncResponse`` class and you can get a output from its method ``processFinish()``. See examples below:
* Read Data
```java
public class ReadDataActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_user);

        // PostResponseAsyncTask readData = new PostResponseAsyncTask(this); // this constructor is deprecated
        PostResponseAsyncTask readData = new PostResponseAsyncTask(this, this); // use this constructor instead
        readData.execute("http://yoursite.com/readdata.php");
    }

    @Override
    public void processFinish(String output) {
        //you can get 'output' from here.
    }
}
```
* POST and get Result
```java
public class PostDataActivity extends AppCompatActivity implements AsyncResponse {

    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("key1", "value1");
        postData.put("key2", "value2");
        
        //You pass postData as the 3nd argument of the constructor
        //PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData); //deprecated
        PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this, postData, this);
        loginTask.execute("http://yoursite.com/post.php");
    }

    @Override
    public void processFinish(String output) {
        //you can get 'output' from here
    }

}
```

# Usage

## Constuctor
### ``PostResponseAsyncTask(Context context, AsyncResponse asyncResponse)``
You have to at least call this constructor and pass in two arguments of a context and an ``AsyncResponse`` object which usually can be done by an anonymous class. Note that the ``AsyncResponse`` is also my custom interface which is not part of Android library at all.

This class is used to get data in String back a result from the web. Because it is async-ed, you can get the data through the AsyncReponse's method called ``processFinish(string output)``. In the method, you get the data via the 'output' String variable.

Here is an example:

```java  
  public class ReadUserActivity extends AppCompatActivity implements AsyncResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_user);

        PostResponseAsyncTask readData = new PostResponseAsyncTask(this, this);
        //For local emulator connecting to local machine, use 10.0.2.2 
        //or for GenyMotion, use 10.0.3.2 instead of yoursite.com
        readData.execute("http://yoursite.com/getdata.php"); 
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_LONG).show();
    }
  }
```
Here is another example of using an anonymous class for the AsyncResponse:
```java
PostResponseAsyncTask readData = new PostResponseAsyncTask(this,
        new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                //TO-DO do something with s
            }
        });
readData.execute("http://yoursite.com/getdata.php");
```

### Exceptions Handler
To connect into a web and retrieve a text from back, this library needs you to catch all 4 exceptions: ``IOException``, ``MalformedURLException``, ``ProtocolException``, and ``UnsupportedEncodingException``. It is very crucial to catch any of them. To handle this, you can use one of methods: ``setEachExceptionsHandler()`` to handle each exception in each override method and ``setExceptionHandler`` to handle any exception. The latter one is not recommended but use it when you are lazy and you just Toast "something went wrong".

#### ``setEachExceptionsHandler(MyEachExceptionsHandler)``
This method lets you catch all 4 exceptions. It is very recommended because you can Toast a message in each error. Below is the sample code:
```java
PostResponseAsyncTask readData = new PostResponseAsyncTask(this, this);
readData.execute("http://yoursite.com/getdata.php");
readData.setEachExceptionsHandler(new EachExceptionsHandler() {
    @Override
    public void handleIOException(IOException e) {
        Toast.makeText(ListActivity.this, "Error with internet or web server.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleMalformedURLException(MalformedURLException e) {
        Toast.makeText(ListActivity.this, "Error with the URL.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleProtocolException(ProtocolException e) {
        Toast.makeText(ListActivity.this, "Error with protocol.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
        Toast.makeText(ListActivity.this, "Error with text encoding.", Toast.LENGTH_LONG).show();
    }
});
```

#### ```setExceptionHandler(MyExceptionHandler)```
This method is used to catch the root Exception class which covers all the exception. It is not recommended. You just a text "Something went wrong" to user whenever there is any exception. You can't tell which really causes the exception. So use it wisely. 
```java
PostResponseAsyncTask readData = new PostResponseAsyncTask(this, this);
readData.execute("http://yoursite.com/getdata.php");
readData.setExceptionHandler(new ExceptionHandler() {
    @Override
    public void handleException(Exception e) {
        Toast.makeText(ListActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
    }
});
```

### ``PostResponseAsyncTask(Context context, AsyncResponse asyncResponse, HashMap<String, String> postData)``
This constructor is used for sending POST data into a web server. This constructor can get a second argument as **HashMap<String, String>**. See an example below:
```java

public class LoginActivity extends AppCompatActivity implements AsyncResponse {

    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> postData = new HashMap<String, String>();
                postData.put("username", etUsername.getText().toString());
                postData.put("password", etPassword.getText().toString() );
                PostResponseAsyncTask loginTask = 
                            new PostResponseAsyncTask(LoginActivity.this, postData, LoginActivity.this);
                loginTask.execute("http://yoursite.com/login.php");
            }
        });
    }

    @Override
    public void processFinish(String output) {
        if(output.equals("success")){
          startActivity(new Intent(this, MainActivity.class));
        }
    }

}
```
### And below is a sample PHP Code:
```php
if(isset($_POST['username']) && isset($_POST['password'])){
    //Connection
    $servername = "yourServer";
    $username = "yourUserName";
    $password = "yourP@ssword";
    $dbname = "YourDBName";
    
    // Create connection
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    // Check connection
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }
    //End Connection
    
    $sql = "SELECT username, password FROM tbl_user WHERE username = ? AND password = ?";

    $stmt = $conn->prepare($sql);

    $stmt->bind_param("ss", $username, $password);
    $username = $_POST['username'];
    $password = $_POST['password'];

    $stmt->execute();

    $stmt->bind_result($username, $password);

    if($stmt->fetch()){
      echo "success";
      exit;
    }
    else{
      echo "failed";
      exit;
    }
}
```

# Set Up

1. Download **GenAsync.jar** from this github to your local machine. 
2. Go to your project in Android Studio, on a Project panel (on the left), go to *app > libs*. You copy the *GenAsync.jar* and paste it in the *libs* folder > Press OK if a asked message appeared.
3. Right click on the jar file > Choose *Add As Library*. And if you are still not sure how to do that, please check out this link http://code2care.org/pages/import-external-jars-to-android-studio-project/.
4. Try this code below:
```java
PostResponseAsyncTask task = new PostResponseAsyncTask(this);
```
5. If it shows no errors and you see  **import com.kosalgeek.asynctask.PostResponseAsyncTask;** correctly, then you are good to go.

# Follow Me
 * Follow me on Twitter https://facebook.com/kosalgeek and https://twitter.com/okosal
 * Subscribe my YouTube channel https://youtube.com/user/oumsaokosal
 * Get more free source codes at https://github.com/kosalgeek
 * Get more tutorials at http://www.kosalgeek.com and http://www.top12review.com
 
# Donation
### If you think this library have saved your life, please support me by donating just only $9.99.
Donate $9.99 [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=oumsaokosal01%40gmail%2ecom&lc=US&item_name=Oum%20Saokosal&item_number=donatedkosal&amount=9%2e99&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)

<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
</form> 

# LICENSE

(The MIT License)
Copyright (c) 2016 KosalGeek. (kosalgeek at gmail dot com)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the 'Software'), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
