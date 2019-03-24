FROM ruby:2.5  
COPY . /var/www/ruby  
WORKDIR /var/www/ruby

RUN bundle install

RUN bundle exec rackup -p 9292 config.ru &

CMD ["ruby","index.rb"]
