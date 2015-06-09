class ExtendedDependencyManagerGrailsPlugin {
    // the plugin version
    def version = "0.5.5-ABZ"
    def grailsVersion = "2.0 > *"

    def title = "Extended Dependency Manager Plugin"
    def author = "David Dawson"
    def authorEmail = "david.dawson@simplicityitself.com"
    def description = '''\
    Provides a simple API in the Grails scripting environment to resolve new libraries from the remote repositories
    and add them to the various classpaths.
'''
    def documentation = "http://grails.org/plugin/dependency-manager"
    def license = "APACHE"
    def organization = [ name: "Simplicity Itself", url: "http://www.simplicityitself.com/" ]
    def issueManagement = [ system: "GitHub", url: "https://github.com/simplicityitself/grails-dependency-manager/issues" ]
    def scm = [ url: "https://github.com/simplicityitself/grails-dependency-manager" ]
}
