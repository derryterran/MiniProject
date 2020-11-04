pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Hello world!'
		 cmd_exec('zip -r ../bundle.zip MiniProject')
      }
    }
  }
} 