includeTargets << grailsScript("_GrailsInit")

dependenciesCache = [:]

addTestDependency = { groupId, artifactId, version, type = "jar" ->
    def files = getDeps(groupId, artifactId, version, type)
    grailsSettings.testDependencies.addAll(files)
    return files
}

addProvidedDependency = { groupId, artifactId, version, type = "jar" ->
    def files = getDeps(groupId, artifactId, version, type)
    grailsSettings.providedDependencies.addAll(files)
    return files
}

addCompileDependency = { groupId, artifactId, version, type = "jar" ->
    def files = getDeps(groupId, artifactId, version, type)
    grailsSettings.compileDependencies.addAll(files)
    return files
}

addRuntimeDependency = { groupId, artifactId, version, type = "jar" ->
    def files = getDeps(groupId, artifactId, version, type)
    grailsSettings.runtimeDependencies.addAll(files)
    return files
}

def getDeps(groupId, artifactId, version, type) {

    def loader = grailsSettings.dependencyManager.getClass().classLoader

    def Dependency
    try {
        Dependency = loader.loadClass("org.sonatype.aether.graph.Dependency")
    } catch (ClassNotFoundException ex) {
        Dependency = loader.loadClass("org.eclipse.aether.graph.Dependency")
    }

    def DefaultArtifact
    try {
        DefaultArtifact = loader.loadClass("org.sonatype.aether.util.artifact.DefaultArtifact")
    } catch (ClassNotFoundException ex) {
        DefaultArtifact = loader.loadClass("org.eclipse.aether.artifact.DefaultArtifact")
    }

    def depId = "${groupId}:${artifactId}:${version}"
    if (!dependenciesCache[depId]) {
        dependenciesCache[depId] = []
        final dependency = Dependency.newInstance(DefaultArtifact.newInstance(
                groupId, artifactId, type, version), "compile")

        grailsSettings.dependencyManager.addDependency(dependency)

        grailsSettings.dependencyManager.resolve("compile").resolvedArtifacts.findAll {
            it.dependency.group == groupId && it.dependency.name == artifactId
        }.each {
            dependenciesCache[depId] << it.file
        }
    }
    dependenciesCache[depId]

}

