import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class StringHandler implements URLHandler {
  List<String> lines;
  String path;
  StringHandler(String path) throws IOException {
    this.path = path;
    this.lines = Files.readAllLines(Paths.get(path));
  }
  public String handleRequest(URI url) throws IOException {
    String query = url.getQuery();
    if(url.getPath().equals("/add")) {
      if(query.startsWith("s=")) {
        String toAdd = query.split("=")[1];
        this.lines.add(toAdd);
        return String.format("%s added, there are now %s lines", toAdd, this.lines.size());
      }
      else {
        return "/add requires a query parameter s";
      }
    }
    else if(url.getPath().equals("/save")) {
      String toSave = String.join("\n", lines);
      Files.write(Paths.get(this.path), toSave.getBytes());
      return "Saved!";
    }
    else if(url.getPath().equals("/search")) {
      if(query.startsWith("q=")) {
        String toSearch = query.split("=")[1];
        String result = "";
        for(String s: lines) {
          if(s.contains(toSearch)) {
            result += s + "\n";
          }
        }
        return result;
      }
      else {
        return "/search requires a query parameter q";
      }
    }
    else {
      return String.join("\n", lines);
    }
  }
}

class StringServer {
  public static void main(String[] args) throws IOException {
    if(args.length == 0){
      System.out.println("Missing port number! Try any number between 1024 to 49151");
      return;
    }

    int port = Integer.parseInt(args[0]);

    Server.start(port, new StringHandler("words.txt"));
  }
}
