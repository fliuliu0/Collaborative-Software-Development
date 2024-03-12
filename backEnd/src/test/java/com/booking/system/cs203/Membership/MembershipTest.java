package com.booking.system.cs203.Membership;

import com.booking.system.cs203.Expenditure.ExpenditureService;
import com.booking.system.cs203.entity.Expenditure;
import com.booking.system.cs203.entity.Membership;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipTest {
    
    @InjectMocks
    private MemberServiceImpl memberService;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private ExpenditureService expenditureService;
    
    public Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); // Year, Month, Day
        return new Date(calendar.getTimeInMillis());
    }

    
    @Test
    void updateMember_TierUpdatedToVIP_ReturnsVIPMember() {
        Long memberId = 500L;
        Membership member = new Membership();
        member.setMemberId(memberId);
        member.setTier("member");
        
        // Get this year
        LocalDateTime currentDateTime = LocalDateTime.now();
        int year = currentDateTime.getYear();
        //Expenditures This year
        Expenditure ex1 = new Expenditure(1L, createDate(year, 3, 3), 20000.0, 500L, null);
        Expenditure ex2 = new Expenditure(2L, createDate(year, 3, 3), 50000.0, 500L, null);
        
        List<Expenditure> expenditures = Arrays.asList(
                ex1,
                ex2
        );
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(expenditureService.getAllExpenditureByMemberId(member.getMemberId())).thenReturn(expenditures);
        when(memberRepository.save(member)).thenReturn(member);
        Membership updatedMember = memberService.updateMember(memberId);
        
        assertEquals("VIP", updatedMember.getTier());
        verify(memberRepository).findById(memberId);
        verify(expenditureService).getAllExpenditureByMemberId(memberId);
        verify(memberRepository).save(member);
    }
    
    @Test
    void findMemberById_MemberExists_ReturnsMember() {
        Long memberId = 1L;
        Membership member = new Membership();
        member.setMemberId(memberId);
        
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        
        Membership foundMember = memberService.findMemberById(memberId);
        
        assertEquals(member, foundMember);
        verify(memberRepository).findById(memberId);
    }
    
    @Test
    void getAllMembers_ReturnsAllMembers() {
        List<Membership> members = Arrays.asList(new Membership(), new Membership());
        
        when(memberRepository.findAll()).thenReturn(members);
        
        List<Membership> fetchedMembers = memberService.getAllMembers();
        
        assertEquals(members.size(), fetchedMembers.size());
        verify(memberRepository).findAll();
    }
    
    @Test
    void createMember_NewMember_SavesNewMember() {
        Long memberId = 1L;
        String tier = "member";
        Membership member = new Membership(memberId, tier);
        
        memberService.createMember(memberId, tier);
        
        verify(memberRepository).save(any(Membership.class));
    }
    
    
}
