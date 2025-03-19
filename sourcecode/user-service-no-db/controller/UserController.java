@RestController
@RequestMapping("v1")
public class UserController {
	@Autowired
	private UserService service;
	@GetMapping("/users")
	public List<User> getUsers() {
		return service.getUsers();
	}
	@GetMapping("/users/{id}")
	public User getUserID(@PathVariable("id") String userId) {
		return service.getUser(userId);
	}
	@PostMapping("/create")
	public void create(@RequestBody User user) {
		service.create(user);
	}
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") String userId) {
		service.delete(userId);
	}
	
	@PatchMapping("/update/{id}")
	public void update(@PathVariable("id") String userId, @RequestBody PatchUserRequest request) {
		service.update(userId, request);
	}
}
