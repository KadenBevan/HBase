import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class UIManager extends Application {
	
	private String _from;
	private String _to;
	private int _defaultWindowSizeV = 500;
	private int _defaultWindowSizeH = 500;
	
	public static void main(String[] args){
		Application.launch(args);
	}
	
	public String get_from() {
		return _from;
	}

	public void set_from(String from) {
		this._from = from;
	}
	
	public String get_to() {
		return _to;
	}

	public void set_clientUsername(String to) {
		this._to = to;
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
	    usernameTF.setPromptText("Username");
	    
	    // - SCENE
	    GridPane root = new GridPane();
	    root.setMinSize(this.get_defaultWindowSizeV(), this.get_defaultWindowSizeH());
	    root.setPadding(new Insets(10, 10, 10, 10));
	    root.setVgap(5);
	    root.setHgap(5);
	    root.setAlignment(Pos.CENTER);
	    
	    root.add(welcomeLabel, 0, 0);
	    root.add(usernameTF, 0, 1);
	    root.add(loginBttn, 0, 2);
	    
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
            		_from = usernameTF.getText().toString(); 
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
		Label welcomeLabel = new Label("Hello! " + get_from());
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
            		_to = usernameTF.getText();
            		try {
						loadMessagePage(primaryStage);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        }); 
	}
	
	/* 
	 * Loads message sending page
	 * @param primaryStage - the application stage
	*/
	private void loadMessagePage(Stage primaryStage) throws IOException{
		
		// - UI
		Label welcomeLabel = new Label("Message to " + get_to() + ":");
		TextField titleTF = new TextField();
		TextField bodyTF = new TextField();
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
		Button refreshBttn = new Button("refresh");
		Button sendMessageBttn = new Button("Send Message");
		Button newMessageBttn = new Button("New Message");
		
		webEngine.setUserStyleSheetLocation(getClass().getResource("message_style.css").toString());
		webEngine.loadContent(getMessagesToHTML(get_from(), get_to()));
		titleTF.setPromptText("Title");
		bodyTF.setPromptText("Body");
	
		// - EVENTS
		sendMessageBttn.setOnAction(new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent arg0) {  

            	if(!titleTF.getText().equals("") && !bodyTF.getText().equals(""))
            	{
            		HBaseManager backend;
					try {
						backend = new HBaseManager();
						backend.addMessage(_from, _to, titleTF.getText().toString(), bodyTF.getText().toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            }
        });
		
		refreshBttn.setOnAction(new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent arg0) {  
            	try {
            		webEngine.loadContent(getMessagesToHTML(get_from(), get_to()));
					webEngine.setUserStyleSheetLocation(getClass().getResource("message_style.css").toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		newMessageBttn.setOnAction(new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent arg0) {  
            	loadWelcomePage(primaryStage);
            }
        });
		
		// - SCENE
		GridPane root = new GridPane();
	    root.setMinSize(this.get_defaultWindowSizeV(), this.get_defaultWindowSizeH());
	    root.setPadding(new Insets(10, 10, 10, 10));
	    root.setVgap(5);
	    root.setHgap(5);
	    root.setAlignment(Pos.CENTER);
	    
	    // add col, row
	    root.add(refreshBttn, 0, 0, 1, 1);
	    root.add(newMessageBttn, 1, 0, 1, 1);
	    root.add(welcomeLabel, 0, 1, 1, 2);
	    root.add(browser, 0, 3, 3, 2);
	    root.add(titleTF, 0, 5, 3, 1);
	    root.add(bodyTF, 0, 7, 3, 1);
	    root.add(sendMessageBttn, 0, 9, 3, 1);
	    
	    Scene scene = new Scene(root,this.get_defaultWindowSizeV(),this.get_defaultWindowSizeV());  
	    primaryStage.setScene(scene);  
	    primaryStage.setTitle("HMessenger - Welcome");  
	    primaryStage.show();
	}
	
	/* 
	 * Calls HBase to get data pertaining to client and user.
	 * @param user - The users username
	 * @param clientUser - The clients username
	 * @return A HTML string of content for message window (WebView/WebEngine)
	*/
	private String getMessagesToHTML(String from, String to) throws IOException{
		
		HBaseManager backend = new HBaseManager();
		
		ArrayList<Map<String, String>> messages = backend.getMessages(from, to);

		// sort response based on time
		Collections.sort(messages, new MessageMapComparator("time"));
		
		// Generate HTML/CSS response
		String content = new String();
		for(Map message : messages){
			if(message.get("to").equals(from)
					&& message.get("from").equals(to)){
				content += "<p class='received'>"+message.get("title")+"<br>"+message.get("body")+"</p>";
			}
			else if(message.get("to").equals(to)
					&& message.get("from").equals(from)){
				content += "<p class='sent'>"+message.get("title")+"<br>"+message.get("body")+"</p>";
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
