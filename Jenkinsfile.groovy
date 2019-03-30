#!/usr/bin/env groovy
import hudson.model.*


node {
    rvmSh 'ruby --version'

    def rvmSh(String rubyVersion, String cmd) {
        def sourceRvm = 'source ~/.rvm/scripts/rvm'
        def useRuby = "rvm use --install $rubyVersion"
        sh "${sourceRvm}; ${useRuby}; $cmd"
    }

}

/*

pipeline {
    agent any
    stages {
      stage('Initialize')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Initialize")
          sh("echo @@@@@@@@@@")
          //sh("docker build .")
          sh '''
            ./bundle
          '''
        }
      }

      stage('Checkout')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Checkout")
          sh("echo @@@@@@@@@@")
        }
      }

      stage('Build')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Build")
          sh("echo @@@@@@@@@@")
          //sh("/usr/local/bin/docker run ruby-app")
        }
      }

      stage('Test')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Test")
          sh("echo @@@@@@@@@@")


            //sh("bundle exec rspec spec")
        }
      }

      stage('Deliver')
      {
        steps {
          sh("echo @@@@@@@@@@")
          sh("echo Deliver")
          sh("echo @@@@@@@@@@")
        }
      }
    }
}

*/
