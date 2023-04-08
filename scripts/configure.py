# This script generates and updates project configuration files.

# Run this script with rvscaffold in PYTHONPATH
import rvscaffold as scaffold

class Project(scaffold.Java):
    def script_path_text(self): return __file__
    def repository_name(self): return 'noexception-slf4j'
    def pretty_name(self): return 'SLF4J extension for NoException'
    def pom_description(self): return 'Exception handlers that log all exceptions into SLF4J loggers.'
    def inception_year(self): return 2022
    def project_status(self): return self.stable_status()
    
    def dependencies(self):
        yield from super().dependencies()
        yield self.use_noexception()
        yield self.use_slf4j()
        yield self.use_junit()
        yield self.use_hamcrest()
        yield self.use_slf4j_test()
    
    # No link to SLF4J, because automatic modules cannot be linked.
    def javadoc_links(self):
        yield from super().javadoc_links()
        yield 'https://noexception.machinezoo.com/javadocs/core/'

Project().generate()
