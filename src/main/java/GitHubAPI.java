import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitHubAPI {
    public static void main(String[] args) throws IOException {
        System.out.println("시작");
        GitHub gitHub = new GitHubBuilder().withJwtToken("c9cbd5158f646095069d589d3eb7aba08f3efcbd").build();
        System.out.println("github접속함");
        GHRepository repo = null;
        try {
            repo = gitHub.getRepository("whiteship/live-study");
            List<GHIssue> issues = repo.getIssues(GHIssueState.ALL);
            System.out.println(issues.size());
            System.out.println("commentCount="+issues.get(0).getCommentsCount());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
