package net.ramptors.appengine;

import static net.ramptors.appengine.Util.preparaParaBúsqueda;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public abstract class CtrlFiltro extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected final String vista;

	public CtrlFiltro(String vista) {
		this.vista = vista;
	}

	protected abstract Query getConsulta();

	protected abstract Map<String, Object> getMap(QueryDocumentSnapshot doc);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			final String filtro = preparaParaBúsqueda(request.getParameter("filtro"));
			final List<Map<String, Object>> lista = getConsulta().get().get().getDocuments().stream().map(this::getMap)
					.filter(map -> (filtro.isEmpty() || Objects.toString(map.get("filtro"), "").isEmpty()) ? true
							: map.get("filtro").toString().contains(filtro))
					.collect(Collectors.toList());
			request.setAttribute("lista", lista);
			request.getRequestDispatcher(vista).forward(request, response);
		} catch (Exception e) {
			Util.procesa(this, request, response, e);
		}
	}
}