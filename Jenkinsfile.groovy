node{
  stage 'buildInDevelopment'
  openshiftBuild(namespace: 'development', buildConfig: 'ruby-docker-app', showBuildLogs: 'true')

  stage 'deployInDevelopment'
  openshiftDeploy(namespace: 'development', deploymentConfig: 'ruby-docker-app')
  openshiftScale(namespace: 'development', deploymentConfig: 'ruby-docker-app',replicaCount: '2')

  stage 'deployInTesting'
  openshiftTag(namespace: 'development', sourceStream: 'ruby-docker-app',  sourceTag: 'latest', destinationStream: 'ruby-docker-app', destinationTag: 'promoteToQA')
  openshiftDeploy(namespace: 'testing', deploymentConfig: 'ruby-docker-app', )
  openshiftScale(namespace: 'testing', deploymentConfig: 'ruby-docker-app',replicaCount: '3')
}
