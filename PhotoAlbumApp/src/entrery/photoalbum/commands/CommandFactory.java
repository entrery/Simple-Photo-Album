package entrery.photoalbum.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entrery.photoalbum.resource.load.ResourceController;

public class CommandFactory {
	
	private static CommandFactory INSTANCE;
	
	private CommandFactory() {	
	}
	
	public static CommandFactory getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new CommandFactory();
		}
		return INSTANCE;
	}
	
	public ICommand createCommand(Action action, HttpServletRequest request, HttpServletResponse response) {
		ICommand command;
		switch (action) {
		case LOAD:
			command = new RetrieveImageCommand(request, response, new ResourceController());
			break;
		case UPLOAD:
			command = new UploadCommand(request, new ResourceController());
			break;
		case CREATE:
			command = new CreateDirectoryCommand(request, new ResourceController());
			break;
		case DELETE:
			command = new DeleteCommand(request, new ResourceController());
			break;
		default:
			throw new IllegalArgumentException("Unknown command");
		}
		return command;
	}
}
