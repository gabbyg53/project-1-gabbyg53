package com.revature.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.daos.ReimbursementDao;
import com.revature.models.Reimbursement;

public class ReimbursementServlet extends HttpServlet {
	
	private ReimbursementDao reimbDao = ReimbursementDao.currentIplementation;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
		System.out.println("To context param: " + req.getServletContext().getInitParameter("To"));

		resp.addHeader("Access-Control-Allow-Origin", "http://localhost:5500");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Reimbursement> reimbs;
		
		//String username = req.getParameter("ers_username"); //username
		String role = req.getParameter("ers_user_role");
		
		if (role != null) { // find by username
			reimbs = reimbDao.findByUsername(role);
		} else { // find all
			reimbs = reimbDao.findAll();
		}
		
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(reimbs);
		
		resp.addHeader("content-type", "application/json");
		resp.getWriter().write(json);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// read the reimbursements from the request body
				ObjectMapper om = new ObjectMapper();
				Reimbursement r = (Reimbursement) om.readValue(req.getReader(), Reimbursement.class);

				System.out.println(r);

				int id = reimbDao.save(r);
				r.setId(id);

				String json = om.writeValueAsString(r);

				resp.getWriter().write(json);
				resp.setStatus(201); // created status code
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ObjectMapper om = new ObjectMapper();
		Reimbursement r = (Reimbursement) om.readValue(req.getReader(), Reimbursement.class);

		System.out.println(r);

		reimbDao.managerUpdateStatus(r.getStatus(), r.getResolver(), r.getId());  //save(r);

		//String json = om.writeValueAsString(r);

		//resp.getWriter().write(json);
		resp.setStatus(201); // created status code
	}

}
