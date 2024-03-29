package java63.web03.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java63.web03.dao.MakerDao;
import java63.web03.dao.ProductDao;
import java63.web03.domain.Product;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/product")
public class ProductControl {
  static Logger log = Logger.getLogger(ProductControl.class);
	static final int PAGE_DEFAULT_SIZE = 3;

	@Autowired MakerDao makerDao;
	@Autowired ProductDao productDao;
	@Autowired ServletContext servletContext;

	@RequestMapping(value="/add", method=RequestMethod.GET)
	public ModelAndView form() throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.addObject("makers", makerDao.selectNameList());
		mv.setViewName("product/ProductForm");
		return mv;
	}

	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String add(Product product) throws Exception {  
		String fileuploadRealPath = 
				servletContext.getRealPath("/fileupload");
		String filename = System.currentTimeMillis() + "_"; 
		File file = new File(fileuploadRealPath + "/" + filename);
		product.getPhotofile().transferTo(file);
		product.setPhoto(filename);

		productDao.insert(product);
		productDao.insertPhoto(product);
		return "redirect:list.do";
	}

	@RequestMapping("/delete")
	public String delete(int no) throws Exception {
		productDao.deletePhoto(no);
		productDao.delete(no);
		return "redirect:list.do";
	}

	@RequestMapping("/list")
	public String list(
			@RequestParam(defaultValue="0") int pageNo,
			@RequestParam(defaultValue="0") int pageSize,
			Model model) throws Exception {

		if (pageNo > 0) {
			pageSize = PAGE_DEFAULT_SIZE;
		}

		HashMap<String,Object> paramMap = new HashMap<>();
		paramMap.put("startIndex", ((pageNo - 1) * pageSize));
		paramMap.put("pageSize", pageSize);

		model.addAttribute("products", productDao.selectList(paramMap));

		return "product/ProductList";
	}

	@RequestMapping("/update")
	public String update(Product product) throws Exception {
		productDao.update(product);
		return "redirect:list.do";
	}

	@RequestMapping("/view")
	public String view(int no, Model model) throws Exception {
		Product product = productDao.selectOne(no);
		model.addAttribute("product", product);
		model.addAttribute("photos", 
				productDao.selectPhoto(product.getNo()));

		model.addAttribute("makers", makerDao.selectNameList());
		return "product/ProductView";
	}

	public static void main(String[] args) throws Exception { 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// setLenient 기본은 true => 날짜가 초과되면 자동으로 계산하여 예외를 표시하지 않는다.
		// false로 설정 => 문자열을 엄격히 검사한다. 형식에 따르지 않을 시 예외 발생.
		//dateFormat.setLenient(false);
		System.out.println(dateFormat.parse("2014-12-40"));
	}
}