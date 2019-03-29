node {
    stage('Initialize')
    {
      script {
        echo "Initialize"
      }
    }

    stage('Checkout')
    {
      script {
        echo "Checkout"
      }
    }

    stage('Build')
    {
      script {
        echo "Build"
      }
    }

    stage('Test')
    {
      script {
        echo "Test"
      }
    }

    stage('Deliver')
    {
      script {
        echo "Deliver"
      }
    }
}
