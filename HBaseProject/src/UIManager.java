import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class UIManager extends Application {
	
	private String _username;
	private String _clientUsername;
	private int _defaultWindowSizeV = 250;
	private int _defaultWindowSizeH = 250;
	
	public static void main(String[] args){
		Application.launch(args);
	}
	
	public String get_username() {
		return _username;
	}

	public void set_username(String _username) {
		this._username = _username;
	}
	
	public String get_clientUsername() {
		return _clientUsername;
	}

	public void set_clientUsername(String _clientUsername) {
		this._clientUsername = _clientUsername;
	}
	
	public int get_defaultWindowSizeV() {
		return _defaultWindowSizeV;
	}

	public void set_defaultWindowSizeV(int _defaultWindowSizeV) {
		this._defaultWindowSizeV = _defaultWindowSizeV;
	}

	public int get_defaultWindowSizeH() {
		return _defaultWindowSizeH;
	}

	public void set_defaultWindowSizeH(int _defaultWindowSizeH) {
		this._defaultWindowSizeH = _defaultWindowSizeH;
	}
	
	/* 
	 * Loads login page
	 * @param primaryStage - the application stage
	*/
	private void loadLoginPage(Stage primaryStage){ 
		
		// - UI
		
		Label welcomeLabel = new Label("Welcome to HMessenger!");
	    TextField usernameTF = new TextField();
	    Button loginBttn = new Button("Login");
	    PasswordField passwordTF = new PasswordField();

	    usernameTF.setPromptText("Username");
	    passwordTF.setPromptText("Password");
	    
	    // - SCENE
	    
	    GridPane root = new GridPane();
	    root.setMinSize(this.get_defaultWindowSizeV(), this.get_defaultWindowSizeH());
	    root.setPadding(new Insets(10, 10, 10, 10));
	    root.setVgap(5);
	    root.setHgap(5);
	    root.setAlignment(Pos.CENTER);
	    
	    root.add(welcomeLabel, 0, 0);
	    root.add(usernameTF, 0, 1);
	    root.add(passwordTF, 0, 2);
	    root.add(loginBttn, 0, 3);
	    
	    Scene scene = new Scene(root,this.get_defaultWindowSizeV(),this.get_defaultWindowSizeH());  
	    primaryStage.setScene(scene);  
	    primaryStage.setTitle("HMessenger - Login");  
	    primaryStage.show();
	    
	    // - EVENTS
	    
	    loginBttn.setOnAction(new EventHandler<ActionEvent>() {  
            
            @Override  
            public void handle(ActionEvent arg0) {  
            	// TODO: -sanitize input from usernameTF
            	//       -call HBaseManager
            	if(!usernameTF.getText().equals(""))
            	{
            		_username = usernameTF.getText().toString(); 
            		loadWelcomePage(primaryStage);
            	}
            }
        }); 
	}
	
	
	/* 
	 * Loads welcome page
	 * @param primaryStage - the application stage
	*/
	private void loadWelcomePage(Stage primaryStage){
		
		// - UI
		Label welcomeLabel = new Label("Hello! " + this.get_username());
		Label instructionLabel = new Label("Who would you like to message?");
		TextField usernameTF = new TextField();
		Button newMessageBttn = new Button("New Message");
		
		usernameTF.setPromptText("Enter Username");
		
		// - SCENE
		GridPane root = new GridPane();
	    root.setMinSize(this.get_defaultWindowSizeV(), this.get_defaultWindowSizeH());
	    root.setPadding(new Insets(10, 10, 10, 10));
	    root.setVgap(5);
	    root.setHgap(5);
	    root.setAlignment(Pos.CENTER);
	    
	    root.add(welcomeLabel, 0, 0);
	    root.add(instructionLabel, 0, 1);
	    root.add(usernameTF, 0, 2);
	    root.add(newMessageBttn, 0, 3);
	    
	    Scene scene = new Scene(root,this.get_defaultWindowSizeV(),this.get_defaultWindowSizeH());  
	    primaryStage.setScene(scene);  
	    primaryStage.setTitle("HMessenger - Welcome");  
	    primaryStage.show(); 
	
		// - EVENTS
		
	    newMessageBttn.setOnAction(new EventHandler<ActionEvent>() {  
            
            @Override  
            
            public void handle(ActionEvent arg0) {  
            	// TODO: -sanitize input from usernameTF
            	//       -call HBaseManager
            	if(!usernameTF.getText().equals(""))
            	{
            		_clientUsername = usernameTF.getText();
            		loadMessagePage(primaryStage);
            	}
            }
        }); 
	}
	
	/* 
	 * Loads message sending page
	 * @param primaryStage - the application stage
	*/
	private void loadMessagePage(Stage primaryStage){
		// - UI
		Label welcomeLabel = new Label("Message to " + this.get_clientUsername() + ":");
		TextField titleTF = new TextField();
		TextField bodyTF = new TextField();
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
		Button sendMessageBttn = new Button("Send Message");
		
		webEngine.setUserStyleSheetLocation(getClass().getResource("message_style.css").toString());
		webEngine.loadContent(this.getContent(this.get_username(), this.get_clientUsername()));
		titleTF.setPromptText("Title");
		bodyTF.setPromptText("Body");
		
		// - SCENE
		GridPane root = new GridPane();
	    root.setMinSize(this.get_defaultWindowSizeV(), this.get_defaultWindowSizeH());
	    root.setPadding(new Insets(10, 10, 10, 10));
	    root.setVgap(5);
	    root.setHgap(5);
	    root.setAlignment(Pos.CENTER);
	    
	    root.add(welcomeLabel, 0, 0);
	    root.add(browser, 0, 1);
	    root.add(titleTF, 0, 2);
	    root.add(bodyTF, 0, 3);
	    root.add(sendMessageBttn, 0, 4);
	    
	    
	    Scene scene = new Scene(root,this.get_defaultWindowSizeV(),this.get_defaultWindowSizeV());  
	    primaryStage.setScene(scene);  
	    primaryStage.setTitle("HMessenger - Welcome");  
	    primaryStage.show(); 
	
		// - EVENTS
	}
	
	/* 
	 * Calls HBase to get data pertaining to client and user.
	 * @param user - The users username
	 * @param clientUser - The clients username
	 * @return A HTML string of content for message window (WebView/WebEngine)
	*/
	private String getContent(String user, String clientUser){
		
		/* get HBaseResponse(user, client)
		 * for(String data : HBaseResponse)
		 *     pack data into Map(username, clientusername, title, body, time)
		 *     add map to list
		 * 
		 * sort list :time
		 * for(Map message : list)
		 * 	   if(from client)
		 * 	   	  contentString += new clientHTML
		 *     else if(from user)
		 *     	  contentString += new userHTML
		 * return content
		 */
		
		// Get data from hbase where:
		// 	(FROM == clientuser & TO == user) | (FROM == user & TO == clientuser)
		ArrayList<Map<String, String>> messages = new ArrayList<Map<String, String>>();
		
		// ---- TEST DATA ----
		Map<String, String> message1 = new HashMap<String, String>();
		message1.put("to", this.get_username());
		message1.put("from", this.get_clientUsername());
		message1.put("title", "Cool Title");
		message1.put("body", "response");
		message1.put("time", "1332775669");
		
		Map<String, String> message2 = new HashMap<String, String>();
		message2.put("to", this.get_clientUsername());
		message2.put("from", this.get_username());
		message2.put("title", "Cool Title");
		message2.put("body", "hello");
		message2.put("time", "1332775667");
		
		Map<String, String> message3 = new HashMap<String, String>();
		message3.put("to", this.get_username());
		message3.put("from", this.get_clientUsername());
		message3.put("title", "Cool Title");
		message3.put("body", "response2");
		message3.put("time", "1332775670");
		
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		
		// ---- TEST DATA ----
		
		// Generate HTML/CSS response
		Collections.sort(messages, new MessageMapComparator("time"));
		
		String content = new String();
		for(Map a : messages){
			if(a.get("to").equals(user)
					&& a.get("from").equals(clientUser)){
				// TODO add from client to user html to content
				content += "<p class='received'>"+a.get("body")+"</p>";
			}
			else if(a.get("to").equals(clientUser)
					&& a.get("from").equals(user)){
				// TODO add to client from user html to content
				content += "<p class='sent'>"+a.get("body")+"</p>";
			}
		}
		
		return content;
	}
		
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("HMessenger");
		loadLoginPage(primaryStage);
	}
	
	class MessageMapComparator implements Comparator<Map<String, String>>
	{
	    private final String key;

	    public MessageMapComparator(String key)
	    {
	        this.key = key;
	    }

	    public int compare(Map<String, String> first, Map<String, String> second)
	    {
	        String firstValue = first.get(key);
	        String secondValue = second.get(key);
	        return firstValue.compareTo(secondValue);
	    }
	}
}
