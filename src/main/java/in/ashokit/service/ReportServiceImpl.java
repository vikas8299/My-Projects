package in.ashokit.service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import in.ashokit.entity.CitizenPlan;
import in.ashokit.repo.CitizenPlanRepository;
import in.ashokit.request.SearchRequest;
import in.ashokit.util.EmailUtils;
import in.ashokit.util.ExcelGenerator;
import in.ashokit.util.PdfGenerator;
@Service
public class ReportServiceImpl implements ReportService {
	
	
	@Autowired
   private CitizenPlanRepository planRepo;
	@Autowired
	private ExcelGenerator excelGenerator;
	@Autowired
	private PdfGenerator pdfGenerator;
	@Autowired
	private EmailUtils emailUtils;
	@Override
	public List<String> getPlanName() {
		
		List<String>planNames= planRepo.getPlanNames();
		return planNames;
	}

	@Override
	public List<String> getPlanStatuses() {
		return planRepo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> search(SearchRequest request) {
		CitizenPlan entity =new CitizenPlan();
		
		if(null!=request.getPlanName() && !"".equals(request.getPlanName())) {
			entity.setPlanName(request.getPlanName());
			}
		
		if(null!=request.getPlanStatus()  &&  !"".equals(request.getPlanStatus())) {
			entity.setPlanStatus(request.getPlanStatus());
			}
		
		if(null!=request.getGender()  &&  !"".equals(request.getGender())) {
			entity.setGender(request.getGender());
			}
		
		if (null !=request.getStartDate()  &&  !"".equals(request.getStartDate())){
			String startDate =request.getStartDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate=LocalDate.parse( startDate, formatter);
			entity.setPlanStartDate(localDate);
		}
		
		if (null !=request.getEndDate()  &&  !"".equals(request.getEndDate())){
			String endDate=request.getEndDate();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate=LocalDate.parse( endDate, formatter);
			entity.setPlanEndDate(localDate);
		}
		
		 return planRepo.findAll(Example.of(entity));
		
	}

	@Override
	public boolean exportExcel(HttpServletResponse response) throws Exception{
		File f = new File("Plans.xls");
           List<CitizenPlan> plans = planRepo.findAll();
           excelGenerator.generate(response,plans,f);
           String subject ="Test -mail subject";
           String body="<h1>Test Mail Body</h1>";
           String to = "vikasmukhrajpatel@gmail.com";
           
          emailUtils.sendEmail(subject, body, to, f);
           f.delete(); 
			return true ;
	}

	@Override
	public boolean exportPdf(HttpServletResponse response)throws Exception {
		File f = new File("Plans.pdf");
		List<CitizenPlan> plans =planRepo.findAll();
		pdfGenerator.generate(response , plans,f);
		   String subject ="Test -mail subject";
           String body="<h1>Test Mail Body</h1>";
           String to = "vikasmukhrajpatel@gmail.com";
           
          emailUtils.sendEmail(subject, body, to, f);
           f.delete(); 
		
				return true;
	}

}
