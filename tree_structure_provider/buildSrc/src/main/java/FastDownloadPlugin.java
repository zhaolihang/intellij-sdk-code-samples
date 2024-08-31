import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.UrlArtifactRepository;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.repositories.ArtifactRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FastDownloadPlugin implements Plugin<Project> {
    private final Map<String, String> urlToFileMap = new HashMap<>();
    Project project;
    @Override
    public void apply(Project project) {
        initializeUrlMap();

        this.project= project;
        project.getRepositories().all(this::interceptRepository);
    }

    private void initializeUrlMap() {
        // 在这里初始化您的 URL 到本地文件的映射
        urlToFileMap.put("https://example.com/file1.jar", "/path/to/local/file1.jar");
        urlToFileMap.put("https://example.com/file2.jar", "/path/to/local/file2.jar");
        // 添加更多映射...
    }

    private void interceptRepository(ArtifactRepository repo) {
        if (repo instanceof UrlArtifactRepository) {
            UrlArtifactRepository urlRepo = (UrlArtifactRepository) repo;
            String repoUrl = urlRepo.getUrl().toString();
            project.getLogger().info("zlh-debug repoUrl :" +repoUrl);

            if (urlToFileMap.containsKey(repoUrl)) {
                String localPath = urlToFileMap.get(repoUrl);
                File localFile = new File(localPath);
                if (localFile.exists()) {
                    project.getLogger().lifecycle("Using local file for repository: " + repoUrl);
                    // 这里我们可以考虑使用 flatDir 仓库来替换原有仓库
                    project.getRepositories().flatDir(flatRepo -> {
                        flatRepo.setName(repo.getName() + "-local");
                        flatRepo.dir(localFile.getParentFile());
                    });
                    // 移除原有仓库
                    project.getRepositories().remove(repo);
                } else {
                    project.getLogger().warn("Mapped local file not found: " + localPath);
                }
            }
        }
    }
}