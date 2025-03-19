@RestController
@RequestMapping("/v1")
public class UserController {
	@Autowired
	private UserService service;
	@GetMapping("/users")
	public List<User> getUsers() {
		return service.getUsers();
	}
	@GetMapping("/users/{id}")
	public User getUserId(@PathVariable("id") String userId) {
		return service.getUser(Long.valueOf(userId));
	}
	@GetMapping("/users/fname/{fname}")
	public User getUserByFirstName(@PathVariable("fname") String firstName) {
		return service.getUserByFirstName(firstName);
	}
	@PostMapping("/create")
	public void create(@RequestBody User user) {
		service.create(user);
	}
	@PatchMapping("/update/{id}")
	public void update(@PathVariable("id") String userId, 
			@RequestBody PatchUserRequest request) {
		User user = service.getUser(Long.valueOf(userId));
		if (user != null) {
			service.update(user, request);
		}
	}
	@DeleteMapping("/delete/{id}")
	//@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") String userId) {
		User user = service.getUser(Long.valueOf(userId));
		if (user != null) {
			service.delete(user.getUserId());
		}
	}
}
