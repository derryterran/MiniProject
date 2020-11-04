#!/usr/bin/groovy
node { 
  stage('Build') {
    echo 'Hello World'
	 cmd_exec('zip -r bundle.zip ../Test-Multi-Branch_master')
  }
} 
def cmd_exec(command) {
    return bat(returnStdout: true, script: "${command}").trim()
}