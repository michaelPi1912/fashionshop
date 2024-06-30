package com.huuphucdang.fashionshop.service;

import com.huuphucdang.fashionshop.model.entity.Address;
import com.huuphucdang.fashionshop.model.entity.Product;
import com.huuphucdang.fashionshop.model.entity.User;
import com.huuphucdang.fashionshop.model.payload.request.AddressRequest;
import com.huuphucdang.fashionshop.model.payload.response.AddressResponse;
import com.huuphucdang.fashionshop.model.payload.response.ProductResponse;
import com.huuphucdang.fashionshop.repository.AddressRepository;
import com.huuphucdang.fashionshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    public AddressResponse findAllByUser(User user, int page, int size){
        try{
            List<Address> addressList;

            Pageable paging = PageRequest.of(page, size);
            Page<Address> pageAddress = addressRepository.findByUser(user.getId(), paging);
            addressList = pageAddress.getContent();

            return AddressResponse.builder()
                    .addressList(addressList)
                    .currentPage(pageAddress.getNumber())
                    .totalItems(pageAddress.getTotalElements())
                    .totalPages(pageAddress.getTotalPages())
                    .build();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public Address saveAddress(AddressRequest body, User user){

        Address address = new Address();
        address.setAddressType(body.getAddressType());
        address.setCommune(body.getCommune());
        address.setAddressDetail(body.getAddressDetail());
        address.setDistrict(body.getDistrict());
        address.setProvince(body.getProvince());
        address.setUser(user);
        return addressRepository.save(address);
    }
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }

    public void updateAddress(AddressRequest body, UUID id) {
        Address address = addressRepository.findById(id).orElseThrow();

        address.setAddressDetail(body.getAddressDetail());
        address.setCommune(body.getCommune());
        address.setAddressType(body.getAddressType());
        address.setDistrict(body.getDistrict());
        address.setProvince(body.getProvince());

        addressRepository.save(address);
    }
}
