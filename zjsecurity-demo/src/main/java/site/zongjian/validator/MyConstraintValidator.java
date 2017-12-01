package site.zongjian.validator;

import org.springframework.beans.factory.annotation.Autowired;
import site.zongjian.service.HelloService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zongjian
 *
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint, Object> {

	@Autowired
	private HelloService helloService;

	@Override
	public void initialize(MyConstraint constraintAnnotation) {
		System.out.println("my validator init");
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		helloService.greeting("tom");
		System.out.println(value);
		return false;
	}

}
