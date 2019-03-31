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
                actualTest()
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

void actualTest() {
    try {
        timeout(time: 20, unit: 'MINUTES') {
            // Select the default cluster
            openshift.withCluster() {
                openshift.withProject() {
                    // Output the url of the currently selected cluster
                    echo "Using project ${openshift.project()} in cluster with url ${openshift.cluster()}"

                    // create single object in array
                    def bc = [[
                        "kind":"BuildConfig",
                        "apiVersion":"v1",
                        "metadata":[
                            "name":"test",
                            "labels":[
                                "name":"test"
                            ]
                        ],
                        "spec":[
                            "triggers":[],
                            "source":[
                                "type":"Binary"
                            ],
                            "strategy":[
                                "type":"Source",
                                "sourceStrategy":[
                                    "from":[
                                        "kind":"DockerImage",
                                        "name":"centos/ruby-22-centos7"
                                    ]
                                ]
                            ],
                            "output":[
                                "to":[
                                    "kind":"ImageStreamTag",
                                    "name":"test:latest"
                                ]
                            ]
                        ]
                      ]
                    ]
                    def objs = openshift.create( bc )
                    objs.describe()
                }
            }
        }
    }
}
