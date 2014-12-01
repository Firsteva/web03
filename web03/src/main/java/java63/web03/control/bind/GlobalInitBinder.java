package java63.web03.control.bind;

import java.text.SimpleDateFormat;
import java.util.Date;
import java63.web03.control.ProductControl01;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;


// 프론트 컨트롤러에게 조언하는 역할의 클래스는 @ControllerAdvice를 붙인다.
// @ControllerAdvice 가 붙은 클래스는 스프링에서 적절히 메서드를 호출한다.

// 스프링 MVC 프레임워크에게,
// 다음 클래스는 컨트롤러에게 조언을 할 기능이 있는 클래스이므로,
// 클래스를 훝어보고 상황에 맞는 메서드를 호출하라.
@ControllerAdvice
public class GlobalInitBinder {
	static Logger log = Logger.getLogger(ProductControl01.class);
	// => 문자열의 요청 파라미터 값을 java.util.Date으로 바꿀 때는
	// 		CustomDateEditor를 사용하라.
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		log.debug("initBinder() 호출 됨");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		binder.registerCustomEditor(
				Date.class, new CustomDateEditor(dateFormat, true));
	}
}
