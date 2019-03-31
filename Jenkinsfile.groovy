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

void actualTest()
{
    try
    {
        timeout(time: 20, unit: 'MINUTES')
        {
            // Select the default cluster
            openshift.withCluster()
            {
                openshift.withProject()
                {
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

                    // Filter created objects and create a selector which selects only the new DeploymentConfigs
                    def dcs = objects.narrow("dc")
                    echo "Database will run in deployment config: ${dcs.name()}"
                    // Find a least one pod related to the DeploymentConfig and wait it satisfies a condition
                    dcs.related('pods').untilEach(1) {
                                            // untilEach only terminates when each selected item causes the body to return true
                                            if (it.object().status.phase != 'Pending') {
                                            // some example debug of the pod in question
                                                shortname = it.object().metadata.name
                                                echo openshift.rsh("${shortname}", "ps ax").out
                                                return true;
                                            }
                                            return false;
                    }

                    // Print out all pods created by the DC
                    echo "Template created pods: ${dcs.related('pods').names()}"

                    // Show how we can use labels to select as well
                    echo "Finding dc using labels instead: ${openshift.selector('dc',[mylabel:'myvalue']).names()}"

                    echo "DeploymentConfig description"
                    dcs.describe()
                    echo "DeploymentConfig history"
                    dcs.rollout().history()


                    def rubySelector = openshift.selector("bc", "ruby")
                    def builds
                    try {
                        rubySelector.object()
                        builds = rubySelector.related( "builds" )
                    } catch (Throwable t) {
                        // The selector returned from newBuild will select all objects created by the operation
                        nb = openshift.newBuild( "https://github.com/abuzitos/ruby-docker-app", "--name=ruby" )

                        // Print out information about the objects created by newBuild
                        echo "newBuild created: ${nb.count()} objects : ${nb.names()}"

                        // Filter non-BuildConfig objects and create selector which will find builds related to the BuildConfig
                        builds = nb.narrow("bc").related( "builds" )

                    }
                }
            }
        }
    } catch (err) {
            echo "in catch block"
            echo "Caught: ${err}"
            currentBuild.result = 'FAILURE'
            throw err
    }
}
