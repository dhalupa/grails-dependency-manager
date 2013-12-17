includeTargets << grailsScript("_GrailsInit")

addTestDependency = { group, name, version, type = null ->
  getDeps(group, name, version, "compile").each { file ->
    grailsSettings.testDependencies << file
    return [file]
  }
}

addProvidedDependency = { group, name, version, type = null ->
  getDeps(group, name, version, "compile").each { file ->
    grailsSettings.providedDependencies << file
    return [file]
  }
}

addCompileDependency = { group, name, version, type = null ->
  getDeps(group, name, version, "compile").each { file ->
    grailsSettings.compileDependencies << file
    return [file]
  }
}

addRuntimeDependency = { group, name, version, type = null ->
  getDeps(group, name, version, "compile").each { file ->
    grailsSettings.runtimeDependencies << file
    return [file]
  }
}

def getDeps(group, name, version, type="jar") {

  def extraAttrs = type == null ? [:] : ['m:classifier': type]

  def ModuleRevisionId = grailsSettings.dependencyManager.getClass().classLoader.loadClass("org.apache.ivy.core.module.id.ModuleRevisionId")

  def mrid = ModuleRevisionId.newInstance(group, name, version, [:])

  println "DEPS ${mrid} // ${type}"

  addModuleToDependencies(mrid, 'compile')
}

def addModuleToDependencies(mrid, type) {

  def ResolveOptions = grailsSettings.dependencyManager.getClass().classLoader.loadClass("org.apache.ivy.core.resolve.ResolveOptions")
  def DownloadOptions = grailsSettings.dependencyManager.getClass().classLoader.loadClass("org.apache.ivy.core.resolve.DownloadOptions")

  def report = grailsSettings.dependencyManager.resolveEngine.resolve(mrid, ResolveOptions.newInstance(confs: ["compile","master"] as String[], transitive:true, outputReport: true, download: true, useCacheOnly: false), false)

  if (report.hasError()) {
    println "Ivy Extended Dependency resolution has errors on artifact [${mrid}], exiting"
    println report.getUnresolvedDependencies().collect {
      it.problem.printStackTrace()
    }
    exit(1)
  }
  report.artifacts.collect {
    def rep = grailsSettings.dependencyManager.resolveEngine.download(it, DownloadOptions.newInstance(log: DownloadOptions.LOG_DEFAULT))
    rep.localFile
  }
}
