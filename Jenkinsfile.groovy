#!/usr/bin/env groovy
import hudson.model.*

node {
    stage('Ruby Gems') {
      sh 'gem install bundler --no-ri --no-rdoc'
      sh 'bundle install'
    }

    stage('Do Puppet Code Validation') {
      sh 'bundle exec rake validate'
    }

    stage('Do Puppet Code Lint') {
      sh 'bundle exec rake lint'
    }

    stage('Do Puppet OS Specs') {
    sh 'bundle exec rake spec'
    }

    stage('Do Puppet Acceptance') {
    sh 'bundle exec rspec spec/acceptance'
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

          sh("bundle")
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
