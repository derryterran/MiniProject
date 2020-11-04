#!/usr/bin/groovy
node { 
  stage('Build') {
    echo 'Hello World'
	 cmd_exec('zip -r ../bundle.zip dist')
  }
} 
def cmd_exec(command) {
    return bat(returnStdout: true, script: "${command}").trim()
}