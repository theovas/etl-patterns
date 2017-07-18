package patternRecognitionExperiments;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.TransformerConfigurationException;

//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.JDOMException;
//import org.jdom2.input.SAXBuilder;
//import org.jdom2.output.XMLOutputter;
import org.jgrapht.ext.GraphMLExporter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;

import structPatterns.StructPattern;
import flowAnalysis.GraphTransformations;

public class TestExecutor {
	public static String host = "localhost";
	public static int port = 27017;
	public static String dbName = "STRUCT_PATTERNS";
	public static String collections = "allpatterns";

	/**
	 * Takes as an argument a string which is the path of a file (.fp file, as a
	 * result of PAFI tool) and stores all the patterns of that file to a
	 * mongodb database.
	 * 
	 * @param filenameInclPath
	 */
	public static void serializeAndStoreStructPatternsFromFile(String filenameInclPath) {
		Mongo mongo = new Mongo(host, port);
		DB db = mongo.getDB(dbName);
		DBCollection collection = db.getCollection(collections);
		// Genson genson = new Genson();
		ArrayList<StructPattern> sps = GraphTransformations.extractStructPatternsFromFile(filenameInclPath);
		String gml;
		String jsonPrettyPrintString;
		String jsonPrettyPrintString2;
		String jsonPrettyPrintString3;
		for (StructPattern sp : sps) {
			try {
				System.out.println("---------");
				// convert StructPattern to graphML
				gml = sp.toGraphML();
				// System.out.println(gml);
				// System.out.println("---------");
				// add id field and other properties

				// SAXBuilder builder = new SAXBuilder();
				// Document doc = (Document) builder.build(new
				// StringReader(gml));
				// Element rootNode = doc.getRootElement();
				// System.out.println(rootNode.getName());
				// // add new age element
				// Element age = new Element("age").setText("28");
				// rootNode.addContent(age);
				// XMLOutputter xmlOutput = new XMLOutputter();
				// gml = xmlOutput.outputString(doc);
				// System.out.println(gml);

				// convert graphML to json
				JSONObject xmlJSONObj = XML.toJSONObject(gml);
				jsonPrettyPrintString = xmlJSONObj.toString(4);
				// removing dots from field names
				jsonPrettyPrintString2 = jsonPrettyPrintString.replace("attr.name", "attr_name");
				jsonPrettyPrintString3 = jsonPrettyPrintString2.replace("attr.type", "attr_type");
				// System.out.println(jsonPrettyPrintString3);

				// convert JSON to DBObject directly
				DBObject dbObject = (DBObject) JSON.parse(jsonPrettyPrintString3);
				// add properties to the mongo object, other than the graph

				dbObject.put("expType", sp.getExpType());
				dbObject.put("size", sp.getSize());
				dbObject.put("lbl", sp.getLabellingType());
				dbObject.put("threshold", sp.getThreshold());
				dbObject.put("isMax", sp.getIsMax());

				collection.insert(dbObject);

				DBCursor cursorDoc = collection.find();
				while (cursorDoc.hasNext()) {
					// System.out.println("cursordoc next: " +
					// cursorDoc.next());
				}

				// System.out.println("Done");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// catch (JDOMException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		}
		// String spJson = genson.serialize(sp);
		// System.out.println(spJson);
		// break;

	}
	// public void recognizePatternOnFlow
}
