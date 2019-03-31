#FROM ruby:2.5
#COPY . /var/www/ruby
#WORKDIR /var/www/ruby

#RUN bundle install

#RUN bundle exec rackup -p 9292 config.ru &

#CMD ["ruby","index.rb"]


FROM jenkins/jenkins

USER root

RUN apt update && apt install -y apt-transport-https ca-certificates curl gnupg2 software-properties-common
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
RUN apt update && apt install -y docker-ce
RUN usermod -aG docker jenkins

USER jenkins
