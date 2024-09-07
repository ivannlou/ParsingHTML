import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\DELL\\Desktop\\DES 2\\Prueba.html";
        String palabraClave = "Java";
        String archivoClave = "file-" + palabraClave + ".log";

        try {
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));

            List<Integer> positions = searchKeywordInHtml(htmlContent, palabraClave);

            if (positions.isEmpty()) {
                System.out.println("No se encontro la palabra'" + palabraClave + "'.");
            } else {
                System.out.println("Se encontraron " + positions.size() + " de la palabra '" + palabraClave + "'.");
                for (int posicion : positions) {
                    System.out.println("Posicion: " + posicion);
                }
                saveLogFile(archivoClave, filePath, positions);
                System.out.println("Bitacora guardada: " + archivoClave);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private static List<Integer> searchKeywordInHtml(String htmlContent, String keyword) throws IOException {
        List<Integer> posicion = new ArrayList<>();
        HTMLEditorKit.ParserCallback callback = new HTMLEditorKit.ParserCallback() {
            public void handleText(char[] data, int pos) {
                String text = new String(data);
                int i = 0;
                while ((i = text.indexOf(keyword, i)) != -1) {
                    posicion.add(pos + i);
                    i += keyword.length();
                }
            }
        };

        Reader reader = new StringReader(htmlContent);
        new ParserDelegator().parse(reader, callback, true);
        return posicion;
    }

    private static void saveLogFile(String logFileName, String htmlFilePath, List<Integer> positions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName))) {
            writer.write("Posiciones de la palabra clave:\n");
            for (int position : positions) {
                writer.write("Posición: " + position + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar la bitacora: " + e.getMessage());
        }
    }
}
