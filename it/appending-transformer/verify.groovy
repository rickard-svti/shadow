{ project ->

    def jar = file("${buildDir}/distributions/${project.name}-${currentVersion}.jar")

    assert jar.exists()

    def jarFile = zipTree(jar)

    def shadeService = jarFile.matching {
        include { fileTreeElement ->
            fileTreeElement.relativePath.toString() == 'META-INF/services/org.apache.maven.Shade'
        }
    }.files as List

    assert shadeService.size() == 1

    def serviceText = shadeService[0].text
    def providers = serviceText.split('(\r\n)|(\r)|(\n)')
    assert providers.size() == 2
    assert providers[0] == 'one # NOTE: No newline terminates this line/file'
    assert providers[1] == 'two # NOTE: No newline terminates this line/file'

}
