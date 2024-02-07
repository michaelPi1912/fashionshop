package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Address;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.AddressRequest;
import com.huuphucdang.fashionshop.model.payload.response.AddressResponse;
import com.huuphucdang.fashionshop.repository.AddressRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
//    public Address findById(UUID id){
//        Optional<Address> address = addressRepository.findById(id);
//        if(address == null){
//            return null;
//        }
//
//        return address.get();
//    }

    public AddressResponse findAllByUser(String email){
        Optional<User> user = userRepository.findByEmail(email);
        List<Address> addressList = addressRepository.getAllByUser(user.get().getId());
        if(addressList == null){
            return null;
        }

        return AddressResponse
                .builder()
                .addressList(addressList)
                .build();
    }
//    public List<Address> findAll(){
//        return addressRepository.findAll();
//    }

    public Address saveAddress(AddressRequest body, String email){

        Optional<User> user = userRepository.findByEmail(email);
        Address address = new Address();
        address.setAddressType(body.getAddressType());
        address.setCommune(body.getCommune());
        address.setAddressDetail(body.getAddressDetail());
        address.setDistrict(body.getDistrict());
        address.setProvince(body.getProvince());
        address.setUser(user.get());
        return addressRepository.save(address);
    }
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }
}
