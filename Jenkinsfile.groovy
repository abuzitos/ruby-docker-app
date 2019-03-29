node {
    stage('Initialize')
    {
      script {
        echo "Initialize"
        docker build .
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
        docker run ruby-app  
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
