package com.in28minutes.springboot.myfirstwebapp.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;

@Controller
// Napravili smo sesiju kako bi imali ovo name i u listTodos.jsp fajlu. 
// Prije toga smo u WelcomeController.java napravili tu sesiju i koristeci model.put stavili to name da ima vrednost
@SessionAttributes("name")
public class TodoControllerJpa {
	
	private TodoService todoService;
	
	private TodoRepository todoRepository;
	
	public TodoControllerJpa(TodoService todoService, TodoRepository todoRepository) {
		super();
		this.todoService = todoService;
		this.todoRepository = todoRepository;
	}

	@RequestMapping("list-todos")
	public String listAllTodos(ModelMap model) {
		String username = getLoggedInUsername(model);
		
		List<Todo> todos = todoRepository.findByUsername(username);
		model.addAttribute("todos", todos);
		return "listTodos";
	}

	
	
	@RequestMapping(value="add-todo", method = RequestMethod.GET)
	public String showNewTodoPage(ModelMap model) {
		String username = getLoggedInUsername(model);
		//ovaj todo je kreiran jer ovaj todo.jsp trazi todo jer je tamo stavljeno u todo.jsp modelAttribute="todo"
		Todo todo = new Todo(0, username, "", LocalDate.now().plusYears(1), false );
		model.put("todo", todo);
		return "todo";
	}
	 
	@RequestMapping(value="add-todo", method = RequestMethod.POST)
	public String addNewTodoPage(ModelMap model,@Valid Todo todo, BindingResult result) {
		// Ovo Todo todo sto imamo kao parametre ove funkcije, to znaci da smo ovo direktno bindovali za Todo bean sto smo napravili
		// we are telling Spring MVC to bind this directly to Todo Bean
		// da bi to uradili moramo i u todo.jsp da podesimo nesto
		// Moramo da koristimo Spring Boot tag libraries, to mozemod a nadjemo na google taj link tako sto ukucamo to
		// i u todo.jsp file na formi dodamo form:form zato sto u ovom linku stoji prefix form
		// i dodajemo attribut toj formi modelAttribute="todo"
		// i posle isto moramo da mapujemo inpute tamo za one properties iz Todo beana
		// to radimo tako sto form:input path="description" npr.
		// Ovo @Valid to postavljamo kako bi nam radili validacije koje smo postavili u Todo.java, kao npr @Size
		// A ovo BindingResult to koristimo kako bi prikazali taj error u todo.jsp
		// I posle u todo.jsp koristimo Form:errors kako bi prikazali taj error
		if(result.hasErrors()) {
			return "todo";
		}
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
		//todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());
		
		
		// ovo redirect znaci da nas redirectuje na list-todos url, znaci url a ne npr listTodos.jsp
		// ovo ovako kad uradimo on ode na taj url i izvrsi onu funkciju koja je vezana za taj url
		// i tako cemo da dobijemo sve potrebne podatke
		return "redirect:list-todos";
	}
	
	@RequestMapping("delete-todo")
	public String deleteTodo(@RequestParam int id) {
		todoRepository.deleteById(id);
		//todoService.deleteById(id);
		return "redirect:list-todos";
	}
	
	@RequestMapping(value="update-todo", method=RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
		Todo todo = todoRepository.findById(id).get();
		//Todo todo = todoService.findById(id);
		model.addAttribute("todo", todo);
		return "todo";
	}
	
	@RequestMapping(value="update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model,@Valid Todo todo, BindingResult result) {
		if(result.hasErrors()) {
			return "todo";
		}
		String username = getLoggedInUsername(model);
		todo.setUsername(username);
		todoRepository.save(todo);
		//todoService.updateTodo(todo);
		return "redirect:list-todos";
	}
	
	private String getLoggedInUsername(ModelMap model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	} 

}
