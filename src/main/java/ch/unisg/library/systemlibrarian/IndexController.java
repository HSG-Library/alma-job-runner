package ch.unisg.library.systemlibrarian;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

@Controller
public class IndexController {

	@View("index")
	@Get
	public String index() {
		return "index";
	}
}
