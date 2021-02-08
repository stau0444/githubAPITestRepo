import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kohsuke.github.*;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GitHubAPITest {

    private static final String baseUrl = "https://api.github.com/repos/whiteship/live-study";


    public static void main(String[] args) throws IOException, InterruptedException, ParseException {


        GitHubAPITest APITest = new GitHubAPITest();
        Long totalIssueNum = getTotalIssueNum(APITest);
        for (int i =1; i<totalIssueNum; i++){
            String commentInfo = APITest.getUserId(i);
            List<String> commentUserIdList = getCommentUserId(commentInfo);
            System.out.println("------------------------------------");
            System.out.println(commentUserIdList.size());
            for (String userId : commentUserIdList) {
                System.out.println(i+"번째 이슈에 댓글을 단 유저 :"+userId);
            }
        }
    }

    private static Long getTotalIssueNum(GitHubAPITest APITest) throws IOException, InterruptedException, ParseException {
        String commentNumber = APITest.getCommentNumber();
        String jsonString = commentNumber.replace("'", "/");
        JSONParser parser= new JSONParser();
        Object parse = parser.parse(commentNumber);
        JSONObject milestone = (JSONObject) parse;
        Object open_issues = milestone.get("open_issues");
        Object closed_issues = milestone.get("closed_issues");
        Long totalIssueNum = (Long)open_issues+(Long)closed_issues;
        return totalIssueNum;
    }

    private static List<String> getCommentUserId(String s) {

        List<String> commentUserList = new ArrayList<>();
        String jsonString = s.replace("'", "/");
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = (JSONArray) obj;
        for(int i=0; i<jsonArray.size(); i++){
            Object comment = jsonArray.get(i);
            JSONObject jsonObject =(JSONObject)comment;
            Object user= jsonObject.get("user");
            JSONObject o2 = (JSONObject)user;
            String login = (String) o2.get("login");
            commentUserList.add(login);
        }
        return commentUserList;
    }


    private String getUserId(int i)throws IOException, InterruptedException{
        var request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/issues/"+i+"/comments"))
                .GET()
                .build();

        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private String getCommentNumber()throws IOException, InterruptedException{
        var request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/milestones/1"))
                .GET()
                .build();

        var response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }


}
