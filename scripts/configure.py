# This script generates and updates project configuration files.

# We are assuming that project-config is available in sibling directory.
# Checkout from https://github.com/robertvazan/project-config
import pathlib
project_directory = lambda: pathlib.Path(__file__).parent.parent
config_directory = lambda: project_directory().parent/'project-config'
exec((config_directory()/'src'/'java.py').read_text())

project_script_path = __file__
repository_name = lambda: 'noexception-slf4j'
pretty_name = lambda: 'SLF4J extension for NoException'
pom_description = lambda: 'Exception handlers that log all exceptions into SLF4J loggers.'
inception_year = lambda: 2022
jdk_version = lambda: 11
project_status = lambda: stable_status()

def dependencies():
    use_noexception()
    use_slf4j()
    use_junit()
    use_hamcrest()
    use_slf4j_test()

# No link to SLF4J, because automatic modules cannot be linked.
javadoc_links = lambda: [
    *standard_javadoc_links(),
    'https://noexception.machinezoo.com/javadoc/'
]

generate()
