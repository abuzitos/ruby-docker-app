node("docker") {
    //docker.withRegistry('<<your-docker-registry>>', '<<your-docker-registry-credentials-id>>') {

        git url: "https://github.com/abuzitos/ruby-docker-app.git", credentialsId: 'https://abuzitos:rahal98@gmail.com'

        sh "git rev-parse HEAD > .git/commit-id"
        def commit_id = readFile('.git/commit-id').trim()
        println commit_id

        stage "build"
        def app = docker.build "ruby-docker-app"

        stage "publish"
        app.push 'master'
        app.push "${commit_id}"
    }
}

/*
node('docker') {
    checkout scm
    stage('Build') {
        docker.image('ruby').inside {
            sh 'ruby --version'
        }
    }
}
*/

/*
pipeline {

    agent {
      docker { image 'ruby:2.5' }
     }

    stages {
      stage('Initialize')
      {
        steps {
          sh "echo 'Initialize'"
          sh "docker build ."
        }
      }

      stage('Checkout')
      {
        steps {
          sh "echo 'Checkout'"
        }
      }

      stage('Build')
      {
        steps {
          sh "echo 'Build'"
          sh "docker run ruby-app"
        }
      }

      stage('Test')
      {
        steps {
          sh "echo 'Test'"
          sh "bundle exec rspec spec"
        }
      }

      stage('Deliver')
      {
        steps {
          sh "echo 'Deliver'"
        }
      }
    }
}
*/
