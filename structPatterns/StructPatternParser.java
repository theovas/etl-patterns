package structPatterns;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class StructPatternParser {

	public static String host = "localhost";
	public static int port = 27017;
	public static String dbName = "STRUCT_PATTERNS";
	public static String collections = "allpatterns";

	public static StructPattern inputGraph(InputStream stream) {
		StructPattern sp = new StructPattern();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			// reading the nodes
			NodeList nList = doc.getElementsByTagName("node");
			String label = "";
			String label2 = "";
			int id = -1;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				NodeList nList2 = nNode.getChildNodes();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					id = Integer.valueOf(eElement.getElementsByTagName("id")
							.item(0).getTextContent());
					label2 = eElement.getElementsByTagName("data").item(0)
							.getTextContent();
					if (label2.startsWith("vertex_label")) {
						label = label2.substring("vertex_label".length());
					}
					System.out.println("id : "
							+ eElement.getElementsByTagName("id").item(0)
									.getTextContent());
					System.out.println("label : " + label);
				}
				PatternNode pn = new PatternNode(label, id);
				sp.addNode(pn);
			}
			nList = doc.getElementsByTagName("edge");
			String src = "";
			String trg = "";
			int id1;
			int id2;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				NodeList nList2 = nNode.getChildNodes();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					src = eElement.getElementsByTagName("source").item(0)
							.getTextContent();
					trg = eElement.getElementsByTagName("target").item(0)
							.getTextContent();
					System.out.println("src: " + src);
					System.out.println("trg: " + trg);
				}
				id1 = Integer.valueOf(src);
				id2 = Integer.valueOf(trg);
				sp.addEdge(id1, id2);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sp;

	}

	/**
	 * Select from the mongodb database all the struct patterns of expType that
	 * is specified as an argument and return them as an arraylist of
	 * StructPattern objects
	 * 
	 * @param expType
	 * @return
	 */
	public static ArrayList<StructPattern> readStructPatternsFromMongoDB(
			String expType) {
		ArrayList<StructPattern> sps = new ArrayList<StructPattern>();
		Mongo mongo = new Mongo(host, port);
		DB db = mongo.getDB(dbName);
		DBCollection collection = db.getCollection(collections);
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("expType", expType);
		// projects out the extra fields, so that the returned bson contains
		// just the graphml part
		// BasicDBList projectionList = new BasicDBList();
		Object zero = new Integer(0);
		DBObject removeIdProjection = new BasicDBObject().append("expType", 0)
				.append("size", zero).append("lbl", 0)
				.append("threshold", zero).append("isMax", 0);

		System.out.println("removed projection: " + removeIdProjection);
		DBCursor cursorDoc = collection.find(whereQuery, removeIdProjection);
		DBObject next;
		String json;
		JSONObject json2 = null;
		String xml = null;
		InputStream stream;
		StructPattern sp;
		String head;
		while (cursorDoc.hasNext()) {
			next = cursorDoc.next();
			// removing the id field that is automatically added by mongo
			next.removeField("_id");
			System.out.println("new cursordoc next: " + next);
			json = next.toString();
			// json to xml
			try {
				json2 = new JSONObject(json);
				json2.getJSONObject("graphml").remove("xmlns");
				json2.getJSONObject("graphml").remove("xsi:schemaLocation");
				json2.getJSONObject("graphml").remove("xmlns:xsi");
				head = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
				xml = head + XML.toString(json2);
				System.out.println("xml: " + xml);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// xml string to input stream
			stream = new ByteArrayInputStream(xml.getBytes());
			ObjectMapper mapper = new ObjectMapper();
			// try {
			// System.out.println("json: " + json2.toString(4));
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			sp = inputGraph(stream);
			System.out.println("read graph description: ");
			System.out.println(sp.toLabelGraphDescr());
			sps.add(sp);
		}
		return sps;
	}
}
