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
                    //openshift.delete("bc", "test")
                    openshift.delete(bc)



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

                    //make sure we handle empty selectors correctly
                                        def nopods = openshift.selector("pod", [ app: "asdf" ])
                                        nopods.withEach {
                                          echo "should not see this echo"
                                        }

                                        // Raw watch which only terminates when the closure body returns true
                                        builds.watch {
                                            // 'it' is bound to the builds selector.
                                            // Continue to watch until at least one build is detected
                                            if ( it.count() == 0 ) {
                                                return false
                                            }
                                            // Print out the build's name and terminate the watch
                                            echo "Detected new builds created by buildconfig: ${it.names()}"
                                            return true
                                        }

                                        echo "Waiting for builds to complete..."

                                        // Like a watch, but only terminate when at least one selected object meets condition
                                        builds.untilEach {
                                            return it.object().status.phase == "Complete"
                                        }

                                        // Print a list of the builds which have been created
                                        echo "Build logs for ${builds.names()}:"

                                        // Find the bc again, and ask for its logs
                                        def result = rubySelector.logs()

                                        // Each high-level operation exposes stout/stderr/status of oc actions that composed
                                        echo "Result of logs operation:"
                                        echo "  status: ${result.status}"
                                        echo "  stderr: ${result.err}"
                                        echo "  number of actions to fulfill: ${result.actions.size()}"
                                        echo "  first action executed: ${result.actions[0].cmd}"

                                        // The following steps below are geared toward testing of bugs or features that have been introduced
                                        // into the openshift client plugin since its initial release

                                        // exercise oc run path, including verification of proper handling of groovy cps
                                        // var binding (converting List to array)
                                        def runargs1 = []
                                        runargs1 << "jenkins-second-deployment"
                                        runargs1 << "--image=docker.io/openshift/jenkins-2-centos7:latest"
                                        runargs1 << "--dry-run"
                                        runargs1 << "-o yaml"
                                        openshift.run(runargs1)

                                        // FYI - pipeline cps groovy compile does not allow String[] runargs2 =  {"jenkins-second-deployment", "--image=docker.io/openshift/jenkins-2-centos7:latest", "--dry-run"}
                                        String[] runargs2 = new String[4]
                                        runargs2[0] = "jenkins-second-deployment"
                                        runargs2[1] = "--image=docker.io/openshift/jenkins-2-centos7:latest"
                                        runargs2[2] = "--dry-run"
                                        runargs2[3] = "-o yaml"
                                        openshift.run(runargs2)

                                        // add this rollout -w test when v0.9.6 is available in our centos image so
                                        // the overnight tests pass
                                        def dc2Selector = openshift.selector("dc", "jenkins-second-deployment")
                                        if (dc2Selector.exists()) {
                                            openshift.delete("dc", "jenkins-second-deployment")
                                        }
                                        openshift.run("jenkins-second-deployment", "--image=docker.io/openshift/jenkins-2-centos7:latest")
                                        dc2Selector.rollout().status("-w")

                                        // Empty static / selectors are powerful tools to check the state of the system.
                                        // Intentionally create one using a narrow and exercise it.
                                        emptySelector = openshift.selector("pods").narrow("bc")
                                        openshift.failUnless(!emptySelector.exists()) // Empty selections never exist
                                        openshift.failUnless(emptySelector.count() == 0)
                                        openshift.failUnless(emptySelector.names().size() == 0)
                                        emptySelector.delete() // Should have no impact
                                        emptySelector.label(["x":"y"]) // Should have no impact

                                        // sanity check for latest and cancel
                                        dc2Selector.rollout().latest()
                                        sleep 3
                                        dc2Selector.rollout().cancel()

                                        // perform a retry on a failed or cancelled deployment
                                        //dc2Selector.rollout().retry()

                                        // validate some watch/selector error handling
                                        try {
                                            timeout(time: 10, unit: 'SECONDS') {
                                                builds.untilEach {
                                                      sleep(20)
                                                }
                                            }
                                            error( "exception did not escape the watch as expected" )
                                        } catch ( e ) {
                                            // test successful
                                        }
                                        try {
                                            builds.watch {
                                                error( "this should be thrown" )
                                            }
                                            error( "exception did not escape the watch as expected" )
                                        } catch ( e ) {
                                            // test successful
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
