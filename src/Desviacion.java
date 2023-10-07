import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
public class Desviacion extends DefaultHandler {
    static final String CLASS_NAME = Desviacion.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private SAXParser parser = null;
    private String currentElement;
    private String price;
    static ArrayList<String> datos= new ArrayList<>();
    private boolean ok;
    public static void main(String[] args) {
        File xmlFile = new File("cd_catalog.xml");
        Desviacion hdlr = new Desviacion();
        hdlr.proceso(xmlFile);


        /*for(String dato: datos){
            System.out.println(dato);
        }*/
        ArrayList<Double> precios = new ArrayList<>();
        for(String dato: datos){
            double price = Double.parseDouble(dato);
            precios.add(price);
        }
        /*for(double price : precios){
            System.out.println(price);
        }*/
        double media = calcularMedia(precios);
        double dsvEstandar = desviacionEstandar(precios);
        System.out.println("La media del catalogo es: " + media);
        System.out.println("La desviación estándar del catalogo es: " +dsvEstandar);
    }

    public Desviacion(){
        super();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
    }
    private void proceso(File f){
        try {
            parser = factory.newSAXParser();
        } catch (SAXException |ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        try {
            parser.parse(f, this);
        }catch (IOException | SAXException e){
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if (localName.equals("CD")){
            ok = true;
        }
        currentElement = localName;
    }
    @Override
    public void characters(char[] bytes, int start, int length){
        if (currentElement.equals("PRICE")){
            this.price = new String(bytes,start,length);
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName){
        if (localName.equals("CD")){
            datos.add(this.price);
            ok = false;
        }
    }



    static double desviacionEstandar(ArrayList<Double> datos){
        double media = calcularMedia(datos);

        double dif = 0;
        for (double dato : datos){
            dif += Math.pow(dato - media, 2);
        }
        double dsvEstandar = Math.sqrt(dif / datos.size());

        return dsvEstandar;
    }
    static double calcularMedia(ArrayList<Double> datos){
        double suma = 0;
        for (double dato : datos){
            suma += dato;
        }
        return suma / datos.size();
    }
}
