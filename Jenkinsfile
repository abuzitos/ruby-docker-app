pipeline {
  agent { docker { image 'ruby:2.5' } }
  stages {
    stage('requirements') {
      steps {
        sh 'gem install bundler -v 2.0.1'
      }
    }
    stage('build') {
      steps {
        sh 'bundle install'
      }
    }
    stage('test') {
      steps {
        sh 'rspec spec'
      }   
    }
  }
}