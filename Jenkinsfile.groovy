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
        bundle exec rspec spec
      }
    }

    stage('Deliver')
    {
      script {
        echo "Deliver"
      }
    }
}
