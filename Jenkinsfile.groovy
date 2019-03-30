#!/usr/bin/env groovy
import hudson.model.*


node {
    stage ('Clone') {
      checkout scm
    }
    stage ('Build') {
        sh gem install bundler
        sh bundle install
        sh cp config/database-gitlab.yml config/database.yml
        sh bundle exec rake db:create db:migrate RAILS_ENV=test
        sh bundle exec rake test
        ....
    }
    stage ('Tests') {
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
