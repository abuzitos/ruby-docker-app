#!/usr/bin/env groovy
import hudson.model.*

pipeline {
    agent any
    stages {
      stage('Initialize')
      {
        steps {
          script {
              sh("echo @@@@@@@@@@")
              sh("echo Initialize")
              sh("echo @@@@@@@@@@")
        	    openshift.setLockName('openshift-dls-test')
          }
        }
      }

      stage('Checkout')
      {
        steps {
            script {
                sh("echo @@@@@@@@@@")
                sh("echo Initialize")
                sh("echo @@@@@@@@@@")
            }
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
