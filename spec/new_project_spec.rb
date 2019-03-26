require_relative '../User'
require 'date'

describe User do
  it "requires a #first_name" do
    user1 = User.new
    user1.first_name = "Bill"
    expect(user1.first_name).to eq "Bill"
  end
  
  it "requires a #last_name" do
    user1 = User.new
    user1.last_name = "Cartwright"
    expect(user1.last_name).not_to eq "clinton"
  end
  
  it "requires an #dob" do
    user1 = User.new
    user1.dob = Date.parse('1957-07-30') 
    expect(user1.last_name).to be_nil 
  end
  
  it "responds to #position" do
    user1 = User.new
    user1.position = "Center"
    expect(user1.position == "Center").to be true
  end
end
