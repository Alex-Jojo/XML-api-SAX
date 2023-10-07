import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;
import java.io.IOException;
import java.util.logging.Logger;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Tabla extends DefaultHandler {
    static final String CLASS_NAME = Tabla.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(CLASS_NAME);
    //private static Set<String> keys = new HashSet<>();
    private static List<String> keys = new ArrayList<>();
    private SAXParserFactory factory = SAXParserFactory.newInstance();
    private SAXParser parser = null;
    private String currentElement;
    private String currentValue;
    private static ArrayList<String> datos = new ArrayList<>();

    public static void main(String[] args) {
        boolean ok = true;
        while(ok){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Ingrese el nombre de un archivo XML: ");
            String name = scanner.nextLine();

            File xmlFile = new File(name+".xml");
            if(xmlFile.exists()){
                Tabla hdlr = new Tabla();
                hdlr.proceso(xmlFile);

/*
        for (String key : keys) {
            System.out.println(key);
        }

        for (String dato : datos){
            System.out.println(dato);
        }
        */
                String columns[] = new String[keys.size()];
                int c = 0;
                for (String key : keys) {
                    columns[c] = key;
                    c++;
                }
                int filas = datos.size()/ keys.size();
                int i = 0;
                Object[][] contenido = new Object[filas][keys.size()];
                for (int j = 0; j < contenido.length; j++) {
                    for (int k = 0; k < contenido[0].length; k++) {
                        if (i<datos.size()){
                            contenido[j][k] = datos.get(i);
                            i++;
                        }else break;
                    }
                }
        /*for (int j = 0; j < contenido.length; j++) {
            for (int k = 0; k < contenido[0].length; k++) {
                System.out.print(contenido[j][k]+"\t");
            }
            System.out.println();
        }*/
                JFrame frame = new JFrame("Tabla para documentos XML");
                frame.setSize(600,400);
                frame.setResizable(true);
                frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

                JTable table = new JTable(contenido,columns);
                JScrollPane pane = new JScrollPane(table);
                frame.add(pane);
                frame. setVisible(true);
                ok = false;
            }else{
                System.out.println("El archivo no existe");
                ok = true;
            }

        }
    }

    public Tabla(){
        super();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
    }
    private void proceso(File f){
        try {
            parser = factory.newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
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
        currentElement = qName;
        currentValue = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName){
        currentElement = null;
        currentValue = null;
    }

    @Override
    public void characters(char[] ch, int start, int length){
        if (currentElement != null && currentValue != null) {
            String value = new String(ch, start, length);
            if (!value.isEmpty()) {
                datos.add(value);

                String key = currentElement.trim();
                if (!key.isEmpty()) {
                    if (!keys.contains(key)) {
                        keys.add(key);
                    }
                }
            }
        }
    }
}
