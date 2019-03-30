#!/usr/bin/env groovy
import hudson.model.*

node {
  deleteDir()
  env.PUPPET_INSTALL_VERSION = "1.8.0"
  env.PUPPET_INSTALL_TYPE = "agent"

  checkout scm

  withRvm('ruby-2.5') {
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
}

def withRvm(version, cl) {
  withRvm(version, "executor-${env.EXECUTOR_NUMBER}") {
        cl()
    }
}

def withRvm(version, gemset, cl) {
    RVM_HOME='$HOME/.rvm'
    paths = [
        "$RVM_HOME/gems/$version@$gemset/bin",
        "$RVM_HOME/gems/$version@global/bin",
        "$RVM_HOME/rubies/$version/bin",
        "$RVM_HOME/bin",
        "${env.PATH}"
    ]

    def path = paths.join(':')

    // withEnv(["PATH=${env.PATH}:$RVM_HOME", "RVM_HOME=$RVM_HOME"]) {
    withEnv(["PATH=$path:$RVM_HOME", "RVM_HOME=$RVM_HOME"]) {
        sh "set +x; source $RVM_HOME/scripts/rvm; rvm use --create --install --binary $version@$gemset"
    }

    withEnv([
        "PATH=$path",
        "GEM_HOME=$RVM_HOME/gems/$version@$gemset",
        "GEM_PATH=$RVM_HOME/gems/$version@$gemset:$RVM_HOME/gems/$version@global",
        "MY_RUBY_HOME=$RVM_HOME/rubies/$version",
        "IRBRC=$RVM_HOME/rubies/$version/.irbrc",
        "RUBY_VERSION=$version"
        ]) {
        'gem install bundler'
            cl()
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
